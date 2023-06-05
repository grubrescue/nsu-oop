package ru.nsu.fit.smolyakov.evaluationsuite.presenter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.SubjectData;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.assignment.AssignmentStatus;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.lesson.Lesson;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.lesson.LessonStatus;
import ru.nsu.fit.smolyakov.tableprinter.TablePrinter;

import java.io.IOException;
import java.util.ArrayList;

@RequiredArgsConstructor
@NonNull
public class EvaluationPresenter {
    private final SubjectData subjectData;

    public void printAttendance(TablePrinter printer) throws IOException {
        printer.clear();
        printer.setTitle("Group " + subjectData.getGroup().getGroupName() + " attendance");

        var lessonList = new ArrayList<String>();

        lessonList.add("\nStudent\\Lesson");
        lessonList.addAll(
            subjectData.getCourse()
                .getLessons()
                .getList()
                .stream()
                .map(Lesson::getDate)
                .map(date ->
                    "%d\n%d\n%d"
                        .formatted(
                            date.getDayOfMonth(),
                            date.getMonthValue(),
                            date.getYear() % 100
                        )
                )
                .toList()
        );
        lessonList.add("\nTOTAL");

        printer.appendRow(lessonList);


        subjectData.getGroup()
            .getStudentList()
            .forEach(
                student -> {
                    var studentAttendance = new ArrayList<String>();
                    studentAttendance.add(student.getNickName());

                    studentAttendance.addAll(
                        subjectData.getCourse()
                            .getLessons()
                            .getList()
                            .stream()
                            .map(lesson ->
                                student.getLessonStatusByLesson(lesson)
                                    .orElse(
                                        lesson.newLessonStatusInstance()
                                    )
                            )
                            .map(LessonStatus::isBeenOnALesson)
                            .map(b -> b ? "+" : " ")
                            .toList()
                    );

                    studentAttendance.add(Integer.toString(student.calculateAmountOfAttendantLessons()));
                    printer.appendRow(studentAttendance);
                }
            );

        printer.print();
    }

    private String assignmentStatusToCellString(AssignmentStatus assignmentStatus) {
        var softCh = !assignmentStatus.getPass().isSkippedSoftDeadline() ? 's' : '-';
        var hardCh = !assignmentStatus.getPass().isSkippedHardDeadline() ? 'h' : '-';

        var javadocOkCh = assignmentStatus.getGrade().isJavadocPassed() ? 'j' : '-';
        var buildOkCh = assignmentStatus.getGrade().isBuildPassed() ? 'b' : '-';

        char testsOkCh;

        if (!assignmentStatus.getAssignment().isRunTests()) {
            testsOkCh = ' ';
        } else {
            testsOkCh = assignmentStatus.getGrade().isTestsPassed() ? 't' : '-';
        }

        char statusCh;
        if (!assignmentStatus.getPass().isFinished()) {
            statusCh = '?';
        } else if (assignmentStatus.getGrade().isOverridden()) {
            statusCh = '*';
        } else {
            statusCh = ' ';
        }

        return String.format(
            "%c%c %c%c%c %1.1f%c",
            softCh,
            hardCh,
            javadocOkCh,
            buildOkCh,
            testsOkCh,
            assignmentStatus.getGrade().getResultingPoints(),
            statusCh
        );
    }

    public void printEvaluation(TablePrinter printer) throws IOException {
        printer.clear();
        printer.setTitle("Group " + subjectData.getGroup().getGroupName() + " tasks evaluation");

        var heading = new ArrayList<String>();
        heading.add("\n      \\Task\nStudent\\\n");
        heading.addAll(
            subjectData.getCourse()
                .getAssignments()
                .getList()
                .stream()
                .map(assignment ->
                    "%s\npts %.2f\ns %s\nh %s\n"
                        .formatted(
                            assignment.getIdentifier(),
                            assignment.getSolvedPoints(),
                            assignment.getSoftDeadline(),
                            assignment.getHardDeadline()
                        )
                )
                .toList()
        );
        heading.add("\nTOTAL\nPOINTS\n");

        printer.appendRow(heading);

        subjectData.getGroup()
            .getStudentList()
            .forEach(student -> {
                var newRow = new ArrayList<String>();
                newRow.add(student.getNickName());

                newRow.addAll(
                    subjectData.getCourse()
                        .getAssignments()
                        .getList()
                        .stream()
                        .map(assignment ->
                            student.getAssignmentStatusByAssignment(assignment)
                                .orElse(
                                    assignment.newAssignmentStatusInstance()
                                )
                        )
                        .map(this::assignmentStatusToCellString)
                        .toList()
                );

                newRow.add("%.2f".formatted(student.calculateTotalPoints()));
                printer.appendRow(newRow);
            });

        printer.print();
    }

//    public void printAssignmentStatus(AssignmentStatus assignmentStatus, TablePrinter printer) {
//        printer.clear();
//        printer.setTitle("Assignment status");
//
//        printer.appendRow(List.of("Task", assignmentStatus.getAssignment().getIdentifier()));
//        printer.appendRow(
//            List.of(
//                "Soft deadline",
//                (!assignmentStatus.getPass().isSkippedSoftDeadline() ? "ok" : "skip")
//                    + " "
//                    + assignmentStatus.getPass().getStarted()
//                + " (" + assignmentStatus.getAssignment().getSoftDeadline() + ")"
//            )
//        );
//        printer.appendRow(
//            List.of(
//                "Hard deadline",
//                (!assignmentStatus.getPass().isSkippedHardDeadline() ? "ok" : "skip")
//                    + " "
//                    + assignmentStatus.getPass().getFinished()
//                    + " (" + assignmentStatus.getAssignment().getHardDeadline() + ")"
//            )
//        );
//
//        printer.appendRow(
//            List.of(
//                "Build",
//                (!assignmentStatus.getGrade().isBuildPassed() ? "ok" : "fail")
//            )
//        );
//
//        printer.appendRow(
//            List.of(
//                "Javadoc",
//                (!assignmentStatus.getGrade().isJavadocPassed() ? "ok" : "fail")
//            )
//        );
//
//        printer.appendRow(
//            List.of(
//                "Tests",
//                (assignmentStatus.getGrade().isTestsPassed() ? "ok" : "fail")
//                    + " "
//                    + "%.3f".formatted(assignmentStatus.getGrade().getJacocoCoverage().orElse(-666.6))
//            )
//        );
//
//        printer.appendRow(
//            List.of(
//                "Grade",
//                "%.3f".formatted(assignmentStatus.getGrade().getResultingPoints())
//            )
//        );
//
//        printer.print();
//    }
}
