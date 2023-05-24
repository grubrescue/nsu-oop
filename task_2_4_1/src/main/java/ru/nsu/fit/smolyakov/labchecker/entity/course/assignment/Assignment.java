package ru.nsu.fit.smolyakov.labchecker.entity.course.assignment;

import lombok.*;

import java.time.LocalDate;

@Value
@Builder
@NonNull
public class Assignment {
    String identifier;
    LocalDate softDeadline;
    LocalDate hardDeadline;
    @Getter(AccessLevel.NONE)
    String defaultBranch;

    double softDeadlineSkipFine;
    double hardDeadlineSkipFine;

//    double maxPoints;
    double solvedPoints;
    boolean runTests;

    public AssignmentStatus newAssignmentStatusInstance() {
        return newAssignmentStatusInstance(defaultBranch);
    }

    public AssignmentStatus newAssignmentStatusInstance(String branch) {
        return new AssignmentStatus(this, identifier, branch);
    }
}
