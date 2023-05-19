package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
@NonNull
public class Task {
    String identifier;
    LocalDate softDeadline;
    LocalDate hardDeadline;
    TaskInfo defaultTaskInfo;

    double softDeadlineSkipFine;
    double hardDeadlineSkipFine;

    double maxPoints;
    double solvedPoints;
    boolean runTests;
}
