package ru.nsu.fit.smolyakov.labchecker.entity.schedule;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Lesson {
    private LocalDate date;
}
