package ru.nsu.fit.smolyakov.labchecker.entity.course;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class TaskResult {
    private boolean skippedSoftDeadline;
    private boolean skippedHardDeadline;
    private int points;
}
