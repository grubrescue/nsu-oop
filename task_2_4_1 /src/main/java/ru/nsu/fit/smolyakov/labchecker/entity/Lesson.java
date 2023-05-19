package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;

@Value
@NonNull
public class Lesson {
    LocalDate date;
    LessonInfo defaultLessonInfo;

    public Lesson(LocalDate date) {
        this.date = date;
        this.defaultLessonInfo = new LessonInfo(this, false);
    }
}
