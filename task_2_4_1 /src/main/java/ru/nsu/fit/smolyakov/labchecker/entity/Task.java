package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class Task {
    @NonNull String identifier;
    @NonNull LocalDate softDeadline;
    @NonNull LocalDate hardDeadline;
    @NonNull TaskInfo defaultTaskInfo;

    double softDeadlineSkipFine;
    double hardDeadlineSkipFine;

    double maxPoints;
    double solvedPoints;
    boolean runTests;
}
