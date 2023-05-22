package ru.nsu.fit.smolyakov.labchecker.checker;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import ru.nsu.fit.smolyakov.labchecker.entity.AssignmentStatus;
import ru.nsu.fit.smolyakov.labchecker.entity.LessonStatus;
import ru.nsu.fit.smolyakov.labchecker.entity.Student;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Log4j2
@RequiredArgsConstructor
public class EvaluationRunner {
    public final String TMP_DIR = ".checks_tmp/" + System.currentTimeMillis() + "/";
    public final int DAYS_DIFF_ATTENDANCE = 4; // TODO это што

    private final Student student;

    private void evaluateSingleLessonAttendance(LessonStatus lessonStatus, Git git) {
        getCommitsStream(git)
            .map(this::getCommitLocalDate)
            .filter(commitDate ->
                commitDate.isBefore(lessonStatus.getLesson().getDate().plusDays(DAYS_DIFF_ATTENDANCE))
            )
            .filter(commitDate ->
                commitDate.isAfter(lessonStatus.getLesson().getDate().minusDays(DAYS_DIFF_ATTENDANCE))
            )
            .findAny()
            .ifPresent(commitDate -> lessonStatus.beenOnALesson(true));
    }

    private void evaluateAttendance(Student student, Git git) {
        student.getLessonStatusList().forEach(lessonStatus -> evaluateSingleLessonAttendance(lessonStatus, git));
    }

    private String getPathToTask(AssignmentStatus assignmentStatus, Git git) {
        var repoPath = git.getRepository().getDirectory().getAbsolutePath();
        return repoPath.substring(0, repoPath.length() - 4) + "/" + assignmentStatus.getTaskNameAlias();
    }

    private void runGradleEvaluator(AssignmentStatus assignmentStatus, Git git) {
        var gradleRunnerBuilder = GradleRunner.builder()
            .projectPath(getPathToTask(assignmentStatus, git))
            .task(new GradleRunner.GradleTask("build", () -> assignmentStatus.setBuildOk(true)))
            .task(new GradleRunner.GradleTask("javadoc", () -> assignmentStatus.setJavadocOk(true)));

        if (assignmentStatus.getAssignment().isRunTests()) {
            gradleRunnerBuilder.task(new GradleRunner.GradleTask("test", () -> assignmentStatus.setTestsOk(true)));
        }
        gradleRunnerBuilder.build().run();
    }

    private Stream<RevCommit> getCommitsStream(Git git) {
        try {
            return StreamSupport.stream(
                git.log()
                    .call()
                    .spliterator(),
                false
            );
        } catch (GitAPIException e) {
            log.error(student.getNickName() + "on getCommitsStream: " + e.getMessage());
            return Stream.empty();
        }
    }

    private Stream<RevCommit> getTaskAssotiatedCommitsStream(AssignmentStatus assignmentStatus, Git git) {
        try {
            return StreamSupport.stream(
                git.log()
                    .addPath(assignmentStatus.getTaskNameAlias())
                    .call()
                    .spliterator(),
                false
            );
        } catch (GitAPIException e) {
            log.error(student.getNickName() + "on getTaskAssociatedCommitsStream: " + e.getMessage());
            throw new RuntimeException(e); // TODO опять кастомные исключения
        }
    }

    private LocalDate getCommitLocalDate(RevCommit commit) {
        PersonIdent authorIdent = commit.getCommitterIdent();
        Date authorDate = authorIdent.getWhen();
        TimeZone authorTimeZone = authorIdent.getTimeZone();

        return authorDate.toInstant()
            .atZone(authorTimeZone.toZoneId())
            .toLocalDate();
    }

    private void evaluateAssignmentFinishedDate(AssignmentStatus assignmentStatus, Git git) {
        getTaskAssotiatedCommitsStream(assignmentStatus, git)
            .map(this::getCommitLocalDate)
            .max(LocalDate::compareTo)
            .ifPresent(newFinishedDate -> {
                if (assignmentStatus.getFinished().isAfter(newFinishedDate)) {
                    assignmentStatus.setFinished(newFinishedDate);
                }
            }); // TODO в принципе проверка лишняя, ибо это будет проверяться только в мастере
    }

    private void evaluateAssignmentStartedDate(AssignmentStatus assignmentStatus, Git git) {
        getTaskAssotiatedCommitsStream(assignmentStatus, git)
            .map(this::getCommitLocalDate)
            .min(LocalDate::compareTo)
            .ifPresent(newStartedDate -> {
                if (assignmentStatus.getStarted().isAfter(newStartedDate)) {
                    assignmentStatus.setStarted(newStartedDate);
                }
            });
    }

    private void checkoutToBranch(String branch, Git git) {
        try {
            git.checkout()
                .setName(branch)
                .setCreateBranch(true)
                .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                .setStartPoint("origin/" + branch)
                .setForced(true)
                .call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e); // TODO ну понятно
        }
    }

    public void evaluate() { // TODO todo
        log.info("Checking {}'s repo", student.getNickName());
        var dir = new File(TMP_DIR + student.getNickName());

        log.info("Cloning {}", student.getRepoUrl());
        var repoCloneCommand = Git.cloneRepository()
            .setURI(student.getRepoUrl())
            .setDirectory(dir)
            .setCloneAllBranches(true);

        try (Git git = repoCloneCommand.call()) {
            log.info("Switched to master branch");
            evaluateAttendance(student, git);

            student.getAssignmentStatusList()
                .stream()
                .filter(assignmentStatus -> new File(getPathToTask(assignmentStatus, git)).exists())
                .forEach(assignmentStatus -> {
                    log.info("Evaluating {} task", assignmentStatus.getTaskNameAlias());
                    evaluateAssignmentStartedDate(assignmentStatus, git);
                    evaluateAssignmentFinishedDate(assignmentStatus, git);
                    runGradleEvaluator(assignmentStatus, git);
                }
            );

            // docs
            checkoutToBranch(student.getDocsBranch(), git);
            log.info("Switched to {} branch", student.getDocsBranch());

            student.getAssignmentStatusList()
                .stream()
                .filter(assignmentStatus -> new File(getPathToTask(assignmentStatus, git)).exists())
                .forEach(assignmentStatus -> evaluateAssignmentStartedDate(assignmentStatus, git));

            evaluateAttendance(student, git);

            student.getAssignmentStatusList()
                .stream()
                .filter(AssignmentStatus::hasBranch)
                .forEach(
                    assignmentStatus -> {
                        checkoutToBranch(assignmentStatus.getBranch().get(), git);

                        evaluateAssignmentStartedDate(assignmentStatus, git);
                        if (assignmentStatus.getFinished().equals(AssignmentStatus.NOT_STARTED)) {
                            log.info("Evaluating {} task", assignmentStatus.getTaskNameAlias());
                            runGradleEvaluator(assignmentStatus, git); // TODO сделать метод сдана ли лаба или нет
                        }

                        evaluateAttendance(student, git);
                    }
                );
        } catch (InvalidRemoteException e) {
            log.error("Invalid remote exception: " + e.getMessage());
        } catch (TransportException e) {
            log.error("Transport exception: " + e.getMessage());
        } catch (GitAPIException e) {
            log.error("Git API exception: " + e.getMessage());
        }
    }
}
