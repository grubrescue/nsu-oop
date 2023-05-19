package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.*;

import java.time.LocalDate;

@Value
@Builder
@NonNull
public class Assignment {
    String identifier;
    LocalDate softDeadline;
    LocalDate hardDeadline;
    @Getter(AccessLevel.NONE) String defaultBranch;

    double softDeadlineSkipFine;
    double hardDeadlineSkipFine;

    double maxPoints;
    double solvedPoints;
    boolean runTests;

    public AssignmentResult assignmentResultInstance() {
        return assignmentResultInstance(defaultBranch);
    }

    public AssignmentResult assignmentResultInstance(String branch) {
        return new AssignmentResult(this, branch);
    }
}
