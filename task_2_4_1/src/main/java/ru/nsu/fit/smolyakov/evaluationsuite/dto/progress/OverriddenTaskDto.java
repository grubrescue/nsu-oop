package ru.nsu.fit.smolyakov.evaluationsuite.dto.progress;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.time.LocalDate;

@Value
@RequiredArgsConstructor
public class OverriddenTaskDto {
    String taskName;
    @NonFinal
    String identifierAlias;
    @NonFinal
    LocalDate started;
    @NonFinal
    LocalDate finished;
    @NonFinal
    Double points;
    @NonFinal
    boolean noBranch = false;
    @NonFinal
    String message;
    @NonFinal
    String branch;

    void startedAt(String dateString) {
        this.started = LocalDate.parse(dateString);
    }

    void finishedAt(String dateString) {
        this.finished = LocalDate.parse(dateString);
    }

    void solvedWithPoints(double points) {
        this.points = points;
    }
}
