package ru.nsu.fit.smolyakov.evaluationsuite.dto.schedule;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AssignmentDto {
    private final String task;
    private LocalDate softDeadline;
    private LocalDate hardDeadline;

    public AssignmentDto (String task) {
        this.task = task;
    }

    void softDeadlineAt (String dateString) {
        this.softDeadline = LocalDate.parse(dateString);
    }

    void hardDeadlineAt (String dateString) {
        this.hardDeadline = LocalDate.parse(dateString);
    }
}
