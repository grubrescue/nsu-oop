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
                            assignment.assignmentResultInstance()
                        );

                        var softCh = !assignmentStatus.isSkippedSoftDeadline() ? 'S' : '-';
                        var hardCh = !assignmentStatus.isSkippedHardDeadline() ? 'H' : '-';

                        var javadocOkCh = assignmentStatus.isJavadocOk() ? 'j' : '-';
                        var buildOkCh = assignmentStatus.isBuildOk() ? 'b' : '-';
                        var testsOkCh = assignmentStatus.isTestsOk() ? 't' : '-';

//                        var overridenCh = assignmentStatus.isOverridden() ? '*' : ' ';
                        char statusCh;
                        if (assignmentStatus.isOverridden()) {
                            statusCh = '*';
                        } else if (!assignmentStatus.isFinished()) {
                            statusCh = '?';
                        } else {
                            statusCh = ' ';
                        }


                        String resStr =
                            String.format("%c%c %c%c%c %.1f%c",
                                softCh,
                                hardCh,
                                javadocOkCh,
                                buildOkCh,
                                testsOkCh,
                                assignmentStatus.getResultingPoints(),
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
