package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.Value;

import java.time.LocalDate;

@Value
public class Lesson {
    LocalDate date;
    LessonInfo defaultLessonInfo;

    public Lesson(LocalDate date) {
        this.date = date;
        this.defaultLessonInfo = new LessonInfo(this, false);
    }
}
