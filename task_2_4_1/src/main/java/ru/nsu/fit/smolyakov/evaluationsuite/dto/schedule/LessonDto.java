package ru.nsu.fit.smolyakov.evaluationsuite.dto.schedule;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class LessonDto {
    private final LocalDate date;
}
