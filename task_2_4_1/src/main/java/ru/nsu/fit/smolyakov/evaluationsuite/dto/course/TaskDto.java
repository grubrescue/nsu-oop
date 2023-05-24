package ru.nsu.fit.smolyakov.evaluationsuite.dto.course;

import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NoArgsConstructor
public class TaskDto {
    @NonFinal
    String name;
    @NonFinal
    String branch;
    @NonFinal
    String description = "no description provided";
    @NonFinal
    Double points;

    @NonFinal
    boolean runTests = true;
}
