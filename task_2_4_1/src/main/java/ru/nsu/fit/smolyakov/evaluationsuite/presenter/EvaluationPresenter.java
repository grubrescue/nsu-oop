package ru.nsu.fit.smolyakov.evaluationsuite.presenter;

import lombok.NonNull;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.SubjectData;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.assignment.AssignmentStatus;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.lesson.Lesson;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.lesson.LessonStatus;
import ru.nsu.fit.smolyakov.tableprinter.TablePrinter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Presenter for {@link SubjectData}.
 * Prints attendance and evaluation tables.
 *
 * @see SubjectData
 * @see TablePrinter
 */
public class EvaluationPresenter {
    private final SubjectData subjectData;

    /**
     * Creates a new presenter.
     *
     * @param subjectData subject data to present
     */
    public EvaluationPresenter(@NonNull SubjectData subjectData) {
        this.subjectData = subjectData;
    }

    /**
     * Prints evaluation table to given printer.
     * The table is a matrix with students in rows and assignments in columns.
     *
     * <p>"+" means that the student was on a lesson,
     * " " means that the student wasn't.
     *
     * @param printer printer to print to
     * @throws IOException if an I/O error occurs
     */
    public void printAttendance(TablePrinter printer) throws IOException {
        printer.clear();
        printer.setTitle("Group " + subjectData.getGroup().getGroupName()
            + " attendance (" + subjectData.getLastUpdate() + ")");

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

    /**
     * Prints evaluation table to given printer.
     * The table is a matrix of students and assignments; every cell is a status of the assignment.
     *
     * <p>"s" means that the student passed the soft deadline,
     * "h" means that the student passed the hard deadline,
     * "j" means that the student passed the javadoc check,
     * "b" means that the student passed the build check,
     * "t" means that the student passed the tests check,
     * "*" means that the student's grade was overridden,
     * "?" means that the student didn't finish the assignment.
     *
     * <p>Points are calculated by corresponding {@link AssignmentStatus} methods.
     *
     * @param printer printer to print to
     * @throws IOException if an I/O error occurs
     */
    public void printEvaluation(TablePrinter printer) throws IOException {
        printer.clear();
        printer.setTitle("Group " + subjectData.getGroup().getGroupName() +
            " tasks evaluation (" + subjectData.getLastUpdate() + ")");

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
}
