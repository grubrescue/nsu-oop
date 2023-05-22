package ru.nsu.fit.smolyakov.labchecker.checker;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import ru.nsu.fit.smolyakov.labchecker.entity.AssignmentStatus;
import ru.nsu.fit.smolyakov.labchecker.entity.LessonStatus;
import ru.nsu.fit.smolyakov.labchecker.entity.Student;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


@RequiredArgsConstructor
public class EvaluationRunner {
    public final String TMP_DIR = ".checks_tmp/" + System.currentTimeMillis() + "/";
    public final int DAYS_DIFF_ATTENDANCE = 4; // TODO это што

    private static LocalDate millisToLocalDateUtil(long millis) {
        return Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
    }

    private void evaluateLessonAttendance(LessonStatus lessonStatus, Git git) {
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
        student.getLessonStatusList().forEach(lessonStatus -> evaluateLessonAttendance(lessonStatus, git));
    }

    private String getPathToTask(AssignmentStatus assignmentStatus, Git git) {
        var repoPath = git.getRepository().getDirectory().getAbsolutePath();
        return repoPath.substring(0, repoPath.length() - 4) + "/" + assignmentStatus.getTaskNameAlias();
    }

    private void buildTestDocsAssignment(AssignmentStatus assignmentStatus, Git git) {
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e); // TODO опять кастомные исключения
        }
    }

    private LocalDate getCommitLocalDate(RevCommit commit) {
        PersonIdent authorIdent = commit.getAuthorIdent();
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

    public void tmp(Student student) throws GitAPIException, IOException { // TODO todo
        var dir = new File(TMP_DIR + student.getNickName());

        var repoCloneCommand = Git.cloneRepository()
            .setURI(student.getRepoUrl())
            .setDirectory(dir)
            .setCloneAllBranches(true);

        try (Git git = repoCloneCommand.call()) {
            evaluateAttendance(student, git);

            student.getAssignmentStatusList()
                .stream()
                .filter(assignmentStatus -> new File(getPathToTask(assignmentStatus, git)).exists())
                .forEach(assignmentStatus -> {
                    evaluateAssignmentStartedDate(assignmentStatus, git);
                    evaluateAssignmentFinishedDate(assignmentStatus, git);
                    buildTestDocsAssignment(assignmentStatus, git);
                }
            );

            // docs
            var ref = git.checkout()
                .setName(student.getDocsBranch())
                .setCreateBranch(true)
                .call();

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
                        try {
                            git.checkout() // TODO может в метод убрать?
                                .setName(assignmentStatus.getBranch().get()) // TODO лучше не оптионал сделать а эксептион
                                .setCreateBranch(true)
                                .call();

                            evaluateAssignmentStartedDate(assignmentStatus, git);
                            if (assignmentStatus.getFinished().equals(AssignmentStatus.NOT_STARTED)) {
                                buildTestDocsAssignment(assignmentStatus, git); // TODO сделать метод сдана ли лаба или нет
                            }
                        } catch (GitAPIException e) {
                            throw new RuntimeException(e);
                        }
                    }
                );
//
//            System.out.println(ref);
//            System.out.println(git.branchList().call());
        }
    }
}



//    private void buildTestDocsAssignment(AssignmentStatus assignmentStatus, Git git) {
//        var builder = GradleRunner.builder()
//            .projectPath(git.getRepository().getDirectory().getPath() + assignmentStatus.getTaskNameAlias())
//            .task("build")
//            .task("javadoc");
//
//        if (assignmentStatus.getAssignment().isRunTests()) {
//            builder.task("test");
//        }
//
//        var gradleRunner = builder.build();
//        var resultMap = gradleRunner.run();
//
//        var build = resultMap.get("build");
//        var javadoc = resultMap.get("javadoc");
//        var test = resultMap.get("test");
//
//        if (build != null && build) {
//            assignmentStatus.setBuildOk(true);
//        } else if (javadoc != null && javadoc) {
//            assignmentStatus.setJavadocOk(true);
//        } else if (test != null && test) {
//            assignmentStatus.setTestsOk(true);
//        } // TODO переделать чтоб нормально было
//    }


//        Git git = new Git(new FileRepository(".checks_tmp/1684585039294/evangelionexpert/.git"));
//        git.fetch()
//            .f
//
//        var taskStatus = student.getAssignmentStatusList().get(0);
//
//        git.branchList()
//            .call()
//            .forEach(ref -> System.out.println(ref.getName()));
//
//        git.checkout().setName(taskStatus.getBranch()).call();

//        var iter = git.log().addPath(taskStatus.getTaskNameAlias()).call();
//        iter.forEach(commit -> System.out.println(commit));


//        var iter2 = git.log().addPath(taskStatus.getTaskNameAlias()).call();
//        iter2.forEach(commit -> System.out.println(commit));


//            var refList = repo.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
//
//            refList
//                .forEach(ref -> {
//                    System.out.println(ref.getName());
//                    try {
//                        repo.fetch().setRemote(ref.getName()).call();
//                    } catch (GitAPIException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//
//            System.out.println("-----");
//            repo.branchList()
//            .call()
//            .forEach(ref -> System.out.println(ref.getName()));
//            var assignmentStatus = student.getAssignmentStatusList().get(0);
//
//            var branchName = assignmentStatus.getBranch();
//            var checkoutCommand = repo.checkout().setName(branchName);
//
//            try {
//                checkoutCommand.call();
//            } catch (GitAPIException e) {
//                throw new RuntimeException(e);
//            }
