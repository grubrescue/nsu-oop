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

    public AssignmentStatus assignmentResultInstance() {
        return assignmentResultInstance(defaultBranch);
    }

    public AssignmentStatus assignmentResultInstance(String branch) {
        return new AssignmentStatus(this, identifier, branch);
    }
}
