package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonNull
public class LessonStatus {
    Lesson lesson;
    @NonFinal boolean beenOnALesson = false;

    public void beenOnALesson(boolean value) {
        beenOnALesson = value;
    }
}
