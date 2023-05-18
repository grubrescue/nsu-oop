package ru.nsu.fit.smolyakov.labchecker.dto.schedule;

import lombok.*;
import lombok.experimental.NonFinal;

import java.time.LocalDate;

@Value
public class AssignmentDto {
    public AssignmentDto(String task) {
        this.task = task;
    }

    String task;
    @NonFinal LocalDate softDeadline;
    @NonFinal LocalDate hardDeadline;

    private void softDeadlineAt(LocalDate date) {
        this.softDeadline = date;
    }

    private void hardDeadlineAt(LocalDate date) {
        this.hardDeadline = date;
    }

    private void softDeadlineAt(String dateString) {
        this.softDeadline = LocalDate.parse(dateString);
    }

    private void hardDeadlineAt(String dateString) {
        this.hardDeadline = LocalDate.parse(dateString);
    }
}
