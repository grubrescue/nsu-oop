package ru.nsu.fit.smolyakov.evaluationsuite.dto.configuration;

import lombok.Getter;

@Getter
public class EvaluationDto {
    private Double taskSolvedPoints;
    private Double softDeadlineSkipFine;
    private Double hardDeadlineSkipFine;
    private Integer jacocoPassPercentage;
}
