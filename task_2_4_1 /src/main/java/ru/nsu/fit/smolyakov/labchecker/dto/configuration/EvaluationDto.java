package ru.nsu.fit.smolyakov.labchecker.dto.configuration;

import lombok.*;
import lombok.experimental.NonFinal;

@Value
@NoArgsConstructor
public class EvaluationDto {
    @NonFinal double defaultMaxPoints;
    @NonFinal double taskSolvedPoints;
    @NonFinal double softDeadlineSkipFine;
    @NonFinal double hardDeadlineSkipFine;
}
