package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;

@Value
@NonNull
public class Lesson {
    LocalDate date;

    public LessonStatus lessonStatusInstance(boolean beenOnALesson) {
        return new LessonStatus(this, beenOnALesson);
    }

    public LessonStatus lessonStatusInstance() {
        return new LessonStatus(this, false);
    }
}
