package ru.nsu.fit.smolyakov.evaluationsuite.evaluator;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.assignment.AssignmentStatus;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.group.Student;

import java.io.File;
import java.io.IOException;

@Log4j2
public class Evaluator {
    public final String TMP_DIR = ".checks_tmp/" + System.currentTimeMillis() + "/";
    public final static String JACOCO_REPORT_RELATIVE_PATH = "/build/reports/jacoco/test/jacocoTestReport.xml";

    private final Student student;

    public Evaluator(@NonNull Student student) {
        this.student = student;
    }

    private void evaluateAttendance(StudentRepository repo) {
        student.getLessonStatusList()
            .forEach(lessonStatus -> {
                if (repo.isCommittedDuringWeek(lessonStatus.getLesson().getDate())) {
                    lessonStatus.setBeenOnALesson(true);
                }
            });
    }

    private String getPathToTask(AssignmentStatus assignmentStatus, StudentRepository repo) {
        return repo.getAbsolutePath() + "/" + assignmentStatus.getIdentifierAlias();
    }

    private void setJacocoCoverage(AssignmentStatus assignmentStatus, String pathToTask) {
        log.info("Parsing jacoco report for task {}", assignmentStatus.getIdentifierAlias());

        var jacocoReportXml = new File(pathToTask + JACOCO_REPORT_RELATIVE_PATH);
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
            log.info("Report file not found, " +
                "so setting jacoco coverage to 0% (sorry :<)");
        }
    }

    private void runGradleEvaluator(AssignmentStatus assignmentStatus, StudentRepository repo) {
        var pathToTask = getPathToTask(assignmentStatus, repo);
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


    private void evaluateAssignmentFinishedDate(AssignmentStatus assignmentStatus, StudentRepository repo) {
        repo.getLastCommit(assignmentStatus.getIdentifierAlias())
            .map(StudentRepository.Commit::date)
            .ifPresent(newFinishedDate -> {
                if (assignmentStatus.getPass().getFinished().isAfter(newFinishedDate)) {
                    assignmentStatus.getPass().setFinished(newFinishedDate);
                }
            });
    }

    private void evaluateAssignmentStartedDate(AssignmentStatus assignmentStatus, StudentRepository repo) {
        repo.getFirstCommit(assignmentStatus.getIdentifierAlias())
            .map(StudentRepository.Commit::date)
            .ifPresent(newStartedDate -> {
                if (assignmentStatus.getPass().getStarted().isAfter(newStartedDate)) {
                    assignmentStatus.getPass().setStarted(newStartedDate);
                }
            });
    }

    private void evaluateOnMaster(StudentRepository repo) {
        evaluateAttendance(repo);

        student.getAssignmentStatusList()
            .stream()
            .filter(assignmentStatus -> new File(getPathToTask(assignmentStatus, repo)).exists())
            .forEach(assignmentStatus -> {
                    log.info("Evaluating {} task on master", assignmentStatus.getIdentifierAlias());
                    evaluateAssignmentStartedDate(assignmentStatus, repo);
                    evaluateAssignmentFinishedDate(assignmentStatus, repo);
                    runGradleEvaluator(assignmentStatus, repo);
                }
            );
    }
    
    private void evaluateOnDocsBranch(StudentRepository repo) {
        student.getAssignmentStatusList()
            .stream()
            .filter(assignmentStatus -> new File(getPathToTask(assignmentStatus, repo)).exists())
            .forEach(assignmentStatus -> evaluateAssignmentStartedDate(assignmentStatus, repo));

        evaluateAttendance(repo);
    }

    private void evaluateSpecifiedTaskOnItsBranch(StudentRepository repo, AssignmentStatus assignmentStatus) {
        evaluateAssignmentStartedDate(assignmentStatus, repo);
        if (!assignmentStatus.getPass().isFinished()) {
            runGradleEvaluator(assignmentStatus, repo);
        } else {
            log.info("Already ran Gradle; no need to do it again");
        }

        evaluateAttendance(repo);
    }

    public void evaluate() {
        log.info("Checking {}'s repo", student.getNickName());
        var dir = new File(TMP_DIR + student.getNickName());

        log.info("Cloning {}", student.getRepoUrl());
        try (StudentRepository repo = new StudentRepository(student.getRepoUrl(), dir)) {
            log.info("Evaluating on master branch");
            evaluateOnMaster(repo);

            if (repo.checkoutToBranch(student.getDocsBranch())) {
                log.info("Switched to {} branch, evaluating", student.getDocsBranch());
                evaluateOnDocsBranch(repo);
            }

            student.getAssignmentStatusList()
                .forEach(
                    assignmentStatus -> {
                        assignmentStatus.getBranch().ifPresent((branch) -> {
                            if (repo.checkoutToBranch(branch)) {
                                log.info("Evaluating {} task on {} branch",
                                    assignmentStatus.getIdentifierAlias(),
                                    branch
                                );
                                evaluateSpecifiedTaskOnItsBranch(repo, assignmentStatus);
                            }
                        });
                    }
                );
        } catch (Exception e) {
            log.fatal("Git API exception: " + e.getMessage());
        }
    }
}
