package ru.nsu.fit.smolyakov.evaluationsuite.dto.schedule;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class LessonDto {
    private final LocalDate date;

    public LessonDto(LocalDate date) {
        this.date = date;
    }
}
