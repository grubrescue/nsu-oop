package ru.nsu.fit.smolyakov.labchecker.printer;

import lombok.RequiredArgsConstructor;
import ru.nsu.fit.smolyakov.labchecker.entity.MainEntity;

import java.io.PrintStream;

@RequiredArgsConstructor
public class ConsoleEvaluationPrinter {
    private final MainEntity mainEntity;
    private final PrintStream printStream = System.out; // todo tmp

    public final static String CELL_FORMAT = "%-12.12s";
    public final static String CELL_SEPARATOR = " | ";

    public void printAttendance() {
//        var a = mainEntity.get
    }

    public void printEvaluation() {
        var students = mainEntity.getGroup();

        System.out.println("Group " + mainEntity.getGroup().getGroupName());
        System.out.println("Format: soft deadline passed/hard deadline passed/total points");
        printStream.printf(CELL_FORMAT + CELL_SEPARATOR, "Student");

        mainEntity.getCourse()
            .getAssignments()
            .getList()
            .forEach(assignment ->
                printStream.printf(CELL_FORMAT + CELL_SEPARATOR, assignment.getIdentifier())
            );

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

                        var softStr = assignmentStatus.isSkippedSoftDeadline() ? "s-" : "s+";
                        var hardStr = assignmentStatus.isSkippedHardDeadline() ? "h-" : "h+";

                        printStream.printf(CELL_FORMAT + CELL_SEPARATOR,
                            softStr + " " + hardStr + " " + assignmentStatus.getResultingPoints());
                    });

                System.out.println();
            });
    }
}
