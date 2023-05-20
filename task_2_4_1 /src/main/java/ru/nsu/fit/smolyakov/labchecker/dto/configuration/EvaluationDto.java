package ru.nsu.fit.smolyakov.labchecker.dto.configuration;

import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NoArgsConstructor
public class EvaluationDto {
    @NonFinal
    Double defaultMaxPoints;
    @NonFinal
    Double taskSolvedPoints;
    @NonFinal
    Double softDeadlineSkipFine;
    @NonFinal
    Double hardDeadlineSkipFine;
}
