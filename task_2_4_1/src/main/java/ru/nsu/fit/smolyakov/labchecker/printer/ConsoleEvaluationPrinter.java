package ru.nsu.fit.smolyakov.labchecker.printer;

import lombok.RequiredArgsConstructor;
import ru.nsu.fit.smolyakov.labchecker.entity.MainEntity;

import java.io.PrintStream;

@RequiredArgsConstructor
public class ConsoleEvaluationPrinter {
    private final MainEntity mainEntity;
    private final PrintStream printStream;

    public final static String CELL_FORMAT = "%-12.12s";
    public final static String CELL_SEPARATOR = " | ";

    public void printAttendance() {
//        var a = mainEntity.get
    }

    public void printEvaluation() { // TODO написать чтобы было норм
        var students = mainEntity.getGroup();

        // draw table head
        printStream.println("Group " + mainEntity.getGroup().getGroupName());
        printStream.println();

        printStream.printf(CELL_FORMAT + CELL_SEPARATOR, "Student");

        mainEntity.getCourse()
            .getAssignments()
            .getList()
            .forEach(assignment ->
                printStream.printf(CELL_FORMAT + CELL_SEPARATOR, assignment.getIdentifier())
            );

        printStream.printf(CELL_FORMAT, "TOTAL POINTS");
        System.out.println();

        mainEntity.getGroup()
            .getStudentList()
            .forEach(student -> {
                printStream.printf(CELL_FORMAT + CELL_SEPARATOR, student.getNickName());
                mainEntity.getCourse()
                    .getAssignments()
                    .getList()
                    .forEach(assignment -> {
                        var assignmentStatus = student.getAssignmentStatusByAssignment(assignment).orElse(
                            assignment.newAssignmentStatusInstance()
                        );

                        var softCh = !assignmentStatus.getPass().isSkippedSoftDeadline() ? 'S' : '-';
                        var hardCh = !assignmentStatus.getPass().isSkippedHardDeadline() ? 'H' : '-';

                        var javadocOkCh = assignmentStatus.getGrade().isJavadocOk() ? 'j' : '-';
                        var buildOkCh = assignmentStatus.getGrade().isBuildOk() ? 'b' : '-';

                        char testsOkCh;

                        if (!assignmentStatus.getAssignment().isRunTests()) {
                            testsOkCh = ' ';
                        } else {
                            testsOkCh = assignmentStatus.getGrade().isTestsOk() ? 't' : '-';
                        }

//                        var overridenCh = assignmentStatus.isOverridden() ? '*' : ' ';
                        char statusCh;
                        if (!assignmentStatus.getPass().isFinished()) {
                            statusCh = '?';
                        } else if (assignmentStatus.getGrade().isOverridden()) {
                            statusCh = '*';
                        } else {
                            statusCh = ' ';
                        }

                        String resStr =
                            String.format("%c%c %c%c%c %1.1f%c",
                                softCh,
                                hardCh,
                                javadocOkCh,
                                buildOkCh,
                                testsOkCh,
                                assignmentStatus.getGrade().getResultingPoints(),
                                statusCh
                            );

                        printStream.printf(CELL_FORMAT + CELL_SEPARATOR, resStr);
                    });

                printStream.printf(CELL_FORMAT, " = " + student.calculateTotalPoints());
                printStream.println();
            });
    }
}



//        System.out.println("Format: [S]oft deadline passed %n" +
//            "[H]ard deadline passed %n" +
//            "[b]uild ok %n" +
//            "[j]avadoc ok %n" +
//            "[t]ests ok %n");
//        System.out.println("* means overriden points. %n");
