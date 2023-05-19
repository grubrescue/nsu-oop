package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;

@Value
@NonNull
public class Lesson {
    LocalDate date;

    public LessonResult lessonResultInstance(boolean beenOnALesson) {
        return new LessonResult(this, beenOnALesson);
    }

    public LessonResult lessonResultInstance() {
        return new LessonResult(this, false);
    }
}
