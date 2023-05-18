package ru.nsu.fit.smolyakov.labchecker.dto.progress;

import lombok.*;
import lombok.experimental.NonFinal;
import ru.nsu.fit.smolyakov.labchecker.dto.course.TaskDto;

import java.time.LocalDate;

@Value
public class TaskResult {
    public TaskResult(TaskDto taskDto) {
        this.taskDto = taskDto;
    }

    TaskDto taskDto;

    @NonFinal LocalDate started;
    @NonFinal LocalDate passed;
    @NonFinal int points;
}
