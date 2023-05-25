package ru.nsu.fit.smolyakov.evaluationsuite.evaluator;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.assignment.AssignmentStatus;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.lesson.LessonStatus;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.group.Student;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Log4j2
@RequiredArgsConstructor
public class Evaluator {
    public final String TMP_DIR = ".checks_tmp/" + System.currentTimeMillis() + "/";
//    public final int DAYS_DIFF_ATTENDANCE = 4; // TODO это што

    private final Student student;

    private void evaluateSingleLessonAttendance(LessonStatus lessonStatus, Git git) {
        getCommitsStream(git)
            .map(this::getCommitLocalDate)
            .filter(commitDate -> {
                    var lessonDate = lessonStatus.getLesson().getDate();
                    return commitDate.isAfter(
                        lessonDate.minusDays(
                            lessonDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue()
                        )
                    );
                }
            )
            .filter(commitDate -> {
                    var lessonDate = lessonStatus.getLesson().getDate();
                    return commitDate.isBefore(
                        lessonDate.plusDays(
                            DayOfWeek.SUNDAY.getValue() - lessonDate.getDayOfWeek().getValue()
                        )
                    );
                }
            )
            .findAny()
            .ifPresent(ignored -> lessonStatus.beenOnALesson(true));
    }

    private void evaluateAttendance(Student student, Git git) {
        student.getLessonStatusList().forEach(lessonStatus -> evaluateSingleLessonAttendance(lessonStatus, git));
    }

    private String getPathToTask(AssignmentStatus assignmentStatus, Git git) {
        var repoPath = git.getRepository().getDirectory().getAbsolutePath();
        return repoPath.substring(0, repoPath.length() - 4) + "/" + assignmentStatus.getIdentifierAlias();
    }

    private void runGradleEvaluator(AssignmentStatus assignmentStatus, Git git) {
        var gradleRunnerBuilder = GradleRunner.builder()
            .projectPath(getPathToTask(assignmentStatus, git))
            .task(new GradleRunner.GradleTask("build", () -> assignmentStatus.getGrade().setBuildOk(true)))
            .task(new GradleRunner.GradleTask("javadoc", () -> assignmentStatus.getGrade().setJavadocOk(true)));

        if (assignmentStatus.getAssignment().isRunTests()) {
            gradleRunnerBuilder.task(new GradleRunner.GradleTask("test", () -> assignmentStatus.getGrade().setTestsOk(true)));
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
//            log.error(student.getNickName() + "on getCommitsStream: " + e.getMessage());
            throw new RuntimeException(e); // TODO опять кастомные исключения
        }
    }

    private Stream<RevCommit> getTaskAssotiatedCommitsStream(AssignmentStatus assignmentStatus, Git git) {
        try {
            return StreamSupport.stream(
                git.log()
                    .addPath(assignmentStatus.getIdentifierAlias())
                    .call()
                    .spliterator(),
                false
            );
        } catch (GitAPIException e) {
//            log.error(student.getNickName() + "on getTaskAssociatedCommitsStream: " + e.getMessage());
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
                if (assignmentStatus.getPass().getFinished().isAfter(newFinishedDate)) {
                    assignmentStatus.getPass().setFinished(newFinishedDate);
                }
            }); // TODO в принципе проверка лишняя, ибо это будет проверяться только в мастере
    }

    private void evaluateAssignmentStartedDate(AssignmentStatus assignmentStatus, Git git) {
        getTaskAssotiatedCommitsStream(assignmentStatus, git)
            .map(this::getCommitLocalDate)
            .min(LocalDate::compareTo)
            .ifPresent(newStartedDate -> {
                if (assignmentStatus.getPass().getStarted().isAfter(newStartedDate)) {
                    assignmentStatus.getPass().setStarted(newStartedDate);
                }
            });
    }

    private boolean checkoutToBranch(String branch, Git git) {
        try {
            git.checkout()
                .setName(branch)
                .setCreateBranch(true)
                .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                .setStartPoint("origin/" + branch)
                .setForced(true)
                .call();
            return true;
        } catch (RefNotFoundException e) {            // docs
            log.warn("Branch {} not found", branch);
            return false;
        } catch (RefAlreadyExistsException e){
            log.error("Branch {} already exists (надо будет почитать про это)", branch); // TODO switch
            return false;
        } catch (Exception e) {
            log.fatal("Unknown error: {}", e.getMessage());
            return false;
        }
    }

    private void evaluateOnMaster(Git git) {
        evaluateAttendance(student, git);

        student.getAssignmentStatusList()
            .stream()
            .filter(assignmentStatus -> new File(getPathToTask(assignmentStatus, git)).exists())
            .forEach(assignmentStatus -> {
                    log.info("Evaluating {} task", assignmentStatus.getIdentifierAlias());
                    evaluateAssignmentStartedDate(assignmentStatus, git);
                    evaluateAssignmentFinishedDate(assignmentStatus, git);
                    runGradleEvaluator(assignmentStatus, git);
                }
            );
    }

    private void evaluateOnDocsBranch(Git git) {
        student.getAssignmentStatusList()
            .stream()
            .filter(assignmentStatus -> new File(getPathToTask(assignmentStatus, git)).exists())
            .forEach(assignmentStatus -> evaluateAssignmentStartedDate(assignmentStatus, git));

        evaluateAttendance(student, git);
    }

    private void evaluateSpecifiedTaskOnItsBranch(Git git, AssignmentStatus assignmentStatus) {
        evaluateAssignmentStartedDate(assignmentStatus, git);
        if (!assignmentStatus.getPass().isFinished()) {
            runGradleEvaluator(assignmentStatus, git); // TODO сделать метод сдана ли лаба или нет
        }

        evaluateAttendance(student, git);
    }

    public void evaluate() { // TODO зарефакторить
        log.info("Checking {}'s repo", student.getNickName());
        var dir = new File(TMP_DIR + student.getNickName());

        log.info("Cloning {}", student.getRepoUrl());
        var repoCloneCommand = Git.cloneRepository()
            .setURI(student.getRepoUrl())
            .setDirectory(dir)
            .setCloneAllBranches(true);

        try (Git git = repoCloneCommand.call()) {
            log.info("On master branch");
            evaluateOnMaster(git);

            if (checkoutToBranch(student.getDocsBranch(), git)) {
                log.info("Switched to {} branch", student.getDocsBranch());
                evaluateOnDocsBranch(git);
            }

            student.getAssignmentStatusList()
                .forEach(
                    assignmentStatus -> {
                        assignmentStatus.getBranch().ifPresent((branch) -> {
                            if (checkoutToBranch(branch, git)) {
                                log.info("Evaluating {} task", assignmentStatus.getIdentifierAlias());
                                evaluateSpecifiedTaskOnItsBranch(git, assignmentStatus);
                            }
                        });
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
