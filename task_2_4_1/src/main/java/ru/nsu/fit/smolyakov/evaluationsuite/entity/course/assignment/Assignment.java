package ru.nsu.fit.smolyakov.evaluationsuite.entity.course.assignment;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Assignment.
 */
@Value
@Builder
@NonNull
public class Assignment implements Serializable {
    String identifier;
    LocalDate softDeadline;
    LocalDate hardDeadline;
    String defaultBranch;

    double softDeadlineSkipFine;
    double hardDeadlineSkipFine;

    int checkstyleWarningsLimit;
    double jacocoPassCoefficient;
    double solvedPoints;
    boolean runTests;

    /**
     * Creates a new instance of {@link AssignmentStatus} corresponding to this assignment.
     *
     * @return a new instance of {@link AssignmentStatus}
     */
    public AssignmentStatus newAssignmentStatusInstance() {
        return newAssignmentStatusInstance(defaultBranch);
    }

    /**
     * Creates a new instance of {@link AssignmentStatus} corresponding to this assignment.
     *
     * @param branch a branch
     * @return a new instance of {@link AssignmentStatus}
     */
    public AssignmentStatus newAssignmentStatusInstance(String branch) {
        return new AssignmentStatus(this, identifier, branch);
    }
}
