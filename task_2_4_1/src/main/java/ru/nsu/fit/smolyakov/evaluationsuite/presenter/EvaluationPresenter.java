package ru.nsu.fit.smolyakov.evaluationsuite.presenter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.SubjectData;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.assignment.Assignment;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.assignment.AssignmentStatus;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.lesson.Lesson;
import ru.nsu.fit.smolyakov.tableprinter.TablePrinter;

import java.util.ArrayList;

@RequiredArgsConstructor
@NonNull
public class EvaluationPresenter {
    private final SubjectData subjectData;

    public void printAttendance(TablePrinter printer) {
        printer.clear();
        printer.setTitle("Group " + subjectData.getGroup().getGroupName() + " tasks evaluation");

        var lessonList = new ArrayList<String>();

        lessonList.add("");
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
                        date.getYear()
                    )
            )
            .toList();


        printer.appendRow(lessonList);
    }

    private String assignmentStatusToCellString(AssignmentStatus assignmentStatus) {
        var softCh = !assignmentStatus.getPass().isSkippedSoftDeadline() ? 's' : '-';
        var hardCh = !assignmentStatus.getPass().isSkippedHardDeadline() ? 'h' : '-';

        var javadocOkCh = assignmentStatus.getGrade().isJavadocOk() ? 'j' : '-';
        var buildOkCh = assignmentStatus.getGrade().isBuildOk() ? 'b' : '-';

        char testsOkCh;

        if (!assignmentStatus.getAssignment().isRunTests()) {
            testsOkCh = ' ';
        } else {
            testsOkCh = assignmentStatus.getGrade().isTestsOk() ? 't' : '-';
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

    public void printEvaluation(TablePrinter printer) { // TODO написать чтобы было норм
        printer.clear();
        printer.setTitle("Group " + subjectData.getGroup().getGroupName() + " tasks evaluation");

        var heading = new ArrayList<String>();
        heading.add("Student");
        heading.addAll(
            subjectData.getCourse()
                .getAssignments()
                .getList()
                .stream()
                .map(Assignment::getIdentifier)
                .toList()
        );
        heading.add("TOTAL POINTS");

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

                newRow.add(Double.toString(student.calculateTotalPoints()));
                printer.appendRow(newRow);
            });

        printer.print();
    }
}



//        System.out.println("Format: [S]oft deadline passed %n" +
//            "[H]ard deadline passed %n" +
//            "[b]uild ok %n" +
//            "[j]avadoc ok %n" +
//            "[t]ests ok %n");
//        System.out.println("* means overriden points. %n");
