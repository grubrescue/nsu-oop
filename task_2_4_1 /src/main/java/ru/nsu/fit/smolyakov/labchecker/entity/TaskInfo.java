package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.*;
import lombok.experimental.NonFinal;

import java.time.LocalDate;

@Value
@RequiredArgsConstructor
public class TaskInfo {
    @NonNull Task task;
    @NonNull String branch;

    @NonNull @NonFinal LocalDate started
        = LocalDate.MAX;
    @NonNull @NonFinal LocalDate finished
        = LocalDate.MAX;

    public void startedAt(String dateString) {
        this.started = LocalDate.parse(dateString);
    }

    public void finishedAt(String dateString) {
        this.finished = LocalDate.parse(dateString);
    }

    // TODO сделать может флаг чтобы если выставлено то поинты сами считались а иначе чтобы нет я не знаю даже
    // ЛИБО сделать чето по другому
    @Setter
    @NonFinal double overridenTaskPoints = 0;

    @Setter
    @NonFinal String message = "no message";

    public double getTaskPoints() {
        return taskPoints;
    }

    public double getFine() {
        double fine = 0;
        if (this.started.isAfter(this.task.getSoftDeadline())) {
            fine += this.task.getCourse()
        }
    }
}
