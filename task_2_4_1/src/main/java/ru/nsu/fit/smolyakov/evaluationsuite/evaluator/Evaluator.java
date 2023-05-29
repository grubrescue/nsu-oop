package ru.nsu.fit.smolyakov.evaluationsuite.evaluator;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.assignment.AssignmentStatus;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.lesson.LessonStatus;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.group.Student;

import java.io.File;
import java.io.IOException;
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

    private final Student student;

    private void evaluateSingleLessonAttendance (LessonStatus lessonStatus, Git git) {
        getCommitsStream(git)
            .map(this::getCommitLocalDate)
            .filter(commitDate -> {
                    var lessonDate = lessonStatus.getLesson().getDate();

                    return commitDate.isAfter(
                        lessonDate.minusDays(
                            lessonDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue() + 1
                        )
                    );
                }
            )
            .filter(commitDate -> {
                    var lessonDate = lessonStatus.getLesson().getDate();

                    return commitDate.isBefore(
                        lessonDate.plusDays(
                            DayOfWeek.SUNDAY.getValue() - lessonDate.getDayOfWeek().getValue() + 1
                        )
                    );
                }
            )
            .findAny()
            .ifPresent(ignored -> lessonStatus.setBeenOnALesson(true));
    }

    private void evaluateAttendance (Student student, Git git) {
        student.getLessonStatusList().forEach(lessonStatus -> evaluateSingleLessonAttendance(lessonStatus, git));
    }

    private String getPathToTask (AssignmentStatus assignmentStatus, Git git) {
        var repoPath = git.getRepository().getDirectory().getAbsolutePath();
        return repoPath.substring(0, repoPath.length() - 4) + "/" + assignmentStatus.getIdentifierAlias();
        // todo может нормально убрать .git из конца path или пойдет????
    }

    private void setJacocoCoverage (AssignmentStatus assignmentStatus, String pathToTask) {
        log.info("Parsing jacoco report for task {}", assignmentStatus.getIdentifierAlias());

        var jacocoReportXml = new File(pathToTask + "/build/reports/jacoco/test/jacocoTestReport.xml");
        if (jacocoReportXml.exists()) {
            try {
                var jacocoParser = JacocoReportParser.parse(jacocoReportXml);

                jacocoParser.getCoverageByType(JacocoReportParser.CounterType.INSTRUCTION)
                    .ifPresentOrElse(
                        coverage -> {
                            assignmentStatus.getGrade().setJacocoCoverage(coverage);
                            log.info("Setting jacoco coverage to {}%", coverage * 100);
                        },
                        () -> {
                            assignmentStatus.getGrade().setJacocoCoverage(0.0);
                            log.info("No INSTRUCTION counter in report file, " +
                                "so setting jacoco coverage to 0% (sorry :<)");
                        }
                    );

            } catch (IOException e) {
                log.error("Failed to parse jacoco report: {}",
                    e.getMessage()
                );
                log.info("Setting jacoco coverage to 0% (sorry :<)");
                assignmentStatus.getGrade().setJacocoCoverage(0.0);
            }
        } else {
            assignmentStatus.getGrade().setJacocoCoverage(0.0);
            log.info("Report file not found " +
                "so setting jacoco coverage to 0% (sorry :<)");
        }
    }

    private void runGradleEvaluator (AssignmentStatus assignmentStatus, Git git) {
        var pathToTask = getPathToTask(assignmentStatus, git);
        var gradleRunnerBuilder = GradleRunner.builder()
            .projectPath(pathToTask)
            .task(new GradleRunner.GradleTask("build", () -> assignmentStatus.getGrade().setBuildPassed(true)))
            .task(new GradleRunner.GradleTask("javadoc", () -> assignmentStatus.getGrade().setJavadocPassed(true)));

        if (assignmentStatus.getAssignment().isRunTests()) {
            gradleRunnerBuilder
                .task(
                    new GradleRunner.GradleTask(
                        "test",
                        () -> assignmentStatus.getGrade().setTestsPassed()
                    )
                )
                .task(
                    new GradleRunner.GradleTask(
                        "jacocoTestReport",
                        () -> setJacocoCoverage(assignmentStatus, pathToTask)
                    )
                );
        }
        gradleRunnerBuilder.build().run();
    }

    private Stream<RevCommit> getCommitsStream (Git git) {
        try {
            return StreamSupport.stream(
                git.log()
                    .call()
                    .spliterator(),
                false
            );
        } catch (GitAPIException e) {
            log.fatal(student.getNickName() + "on getCommitsStream: " + e.getMessage());
            return Stream.empty();
        }
    }

    private Stream<RevCommit> getTaskAssotiatedCommitsStream (AssignmentStatus assignmentStatus, Git git) {
        try {
            return StreamSupport.stream(
                git.log()
                    .addPath(assignmentStatus.getIdentifierAlias())
                    .call()
                    .spliterator(),
                false
            );
        } catch (GitAPIException e) {
            log.fatal(student.getNickName() + "on getTaskAssociatedCommitsStream: " + e.getMessage());
            return Stream.empty();
        }
    }

    private LocalDate getCommitLocalDate (RevCommit commit) {
        PersonIdent authorIdent = commit.getCommitterIdent();
        Date authorDate = authorIdent.getWhen();
        TimeZone authorTimeZone = authorIdent.getTimeZone();

        return authorDate.toInstant()
            .atZone(authorTimeZone.toZoneId())
            .toLocalDate();
    }

    private void evaluateAssignmentFinishedDate (AssignmentStatus assignmentStatus, Git git) {
        getTaskAssotiatedCommitsStream(assignmentStatus, git)
            .map(this::getCommitLocalDate)
            .max(LocalDate::compareTo)
            .ifPresent(newFinishedDate -> {
                if (assignmentStatus.getPass().getFinished().isAfter(newFinishedDate)) {
                    assignmentStatus.getPass().setFinished(newFinishedDate);
                }
            }); // TODO в принципе проверка лишняя, ибо это будет проверяться только в мастере
    }

    private void evaluateAssignmentStartedDate (AssignmentStatus assignmentStatus, Git git) {
        getTaskAssotiatedCommitsStream(assignmentStatus, git)
            .map(this::getCommitLocalDate)
            .min(LocalDate::compareTo)
            .ifPresent(newStartedDate -> {
                if (assignmentStatus.getPass().getStarted().isAfter(newStartedDate)) {
                    assignmentStatus.getPass().setStarted(newStartedDate);
                }
            });
    }

    private boolean checkoutToBranch (String branch, Git git) {
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
        } catch (RefAlreadyExistsException e) {
            log.error("Branch {} already exists", branch); // TODO может быть, сделать switch??
            return false;
        } catch (Exception e) {
            log.fatal("Unknown error: {}", e.getMessage());
            return false;
        }
    }

    private void evaluateOnMaster (Git git) {
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

    private void evaluateOnDocsBranch (Git git) {
        student.getAssignmentStatusList()
            .stream()
            .filter(assignmentStatus -> new File(getPathToTask(assignmentStatus, git)).exists())
            .forEach(assignmentStatus -> evaluateAssignmentStartedDate(assignmentStatus, git));

        evaluateAttendance(student, git);
    }

    private void evaluateSpecifiedTaskOnItsBranch (Git git, AssignmentStatus assignmentStatus) {
        evaluateAssignmentStartedDate(assignmentStatus, git);
        if (!assignmentStatus.getPass().isFinished()) {
            runGradleEvaluator(assignmentStatus, git);
        }

        evaluateAttendance(student, git);
    }

    public void evaluate () {
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
