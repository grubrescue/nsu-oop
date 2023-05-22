package ru.nsu.fit.smolyakov.labchecker.dto.schedule;

import lombok.Value;
import lombok.experimental.NonFinal;

import java.time.LocalDate;

@Value
public class AssignmentDto {
    String task;
    @NonFinal
    LocalDate softDeadline;
    @NonFinal
    LocalDate hardDeadline;
    public AssignmentDto(String task) {
        this.task = task;
    }

    void softDeadlineAt(String dateString) {
        this.softDeadline = LocalDate.parse(dateString);
    }

    void hardDeadlineAt(String dateString) {
        this.hardDeadline = LocalDate.parse(dateString);
    }
}
