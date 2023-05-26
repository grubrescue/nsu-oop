package ru.nsu.fit.smolyakov.evaluationsuite.dto.configuration;

import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NoArgsConstructor
public class EvaluationDto {
    @NonFinal
    Double taskSolvedPoints;
    @NonFinal
    Double softDeadlineSkipFine;
    @NonFinal
    Double hardDeadlineSkipFine;
    @NonFinal
    Integer jacocoPassPercentage;
}
