package ru.nsu.fit.smolyakov.labchecker.entity.tasks;

public record TaskResult(
    boolean skippedSoftDeadline,
    boolean skippedHardDeadline,
    boolean taskSolved,
    int points
) {
}
