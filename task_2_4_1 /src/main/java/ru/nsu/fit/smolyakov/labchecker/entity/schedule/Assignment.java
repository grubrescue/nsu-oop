package ru.nsu.fit.smolyakov.labchecker.entity.schedule;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
public class Assignment {
    private LocalDate softDeadline;
    private LocalDate hardDeadline;

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
