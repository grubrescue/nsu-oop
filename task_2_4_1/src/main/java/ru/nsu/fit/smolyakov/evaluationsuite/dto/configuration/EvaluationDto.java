package ru.nsu.fit.smolyakov.evaluationsuite.dto.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluationDto {
    private Double taskSolvedPoints;
    private Double softDeadlineSkipFine;
    private Double hardDeadlineSkipFine;
    private Integer jacocoPassPercentage;
    private Integer checkstyleErrorsLimit;
}
