package ru.nsu.fit.smolyakov.labchecker.entity.configuration;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Evaluation {
    private final double defaultMaxPoints = 1.0;
    private final double taskSolvedPoints = 1.0;
    private final double softDeadlineSkipFine = -0.5;
    private final double hardDeadlineSkipFine = -0.5;
}
