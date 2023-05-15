package ru.nsu.fit.smolyakov.labchecker.entity.configuration;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Evaluation {
    private double defaultMaxPoints = 1.0;
    private double taskSolvedPoints = 1.0;
    private double softDeadlineSkipFine = -0.5;
    private double hardDeadlineSkipFine = -0.5;
}
