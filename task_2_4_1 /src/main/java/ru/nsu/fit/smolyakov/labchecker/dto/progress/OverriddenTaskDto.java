package ru.nsu.fit.smolyakov.labchecker.dto.progress;

import lombok.Value;
import lombok.experimental.NonFinal;

import java.time.LocalDate;

@Value
public class OverriddenTaskDto {
    String taskName;
    @NonFinal
    String taskNameAlias;
    @NonFinal
    LocalDate started;
    @NonFinal
    LocalDate finished;
    @NonFinal
    Double points;
    @NonFinal
    boolean noBranch = false;
    @NonFinal
    String message = "(нет сообщ в конфиге, потом убрать это)";
    @NonFinal
    String branch;
    public OverriddenTaskDto(String taskName) {
        this.taskName = taskName;
    }

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
