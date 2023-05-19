package ru.nsu.fit.smolyakov.labchecker.dto.progress;

import lombok.*;
import lombok.experimental.NonFinal;

import java.time.LocalDate;

@Value
public class OverriddenTaskDto {
    public OverriddenTaskDto(String taskName) {
        this.taskName = taskName;
    }

    String taskName;

    @NonFinal LocalDate started;
    @NonFinal LocalDate finished;
    @NonFinal Double points;

    @NonFinal String message = "no message";
    @NonFinal String branch;

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
