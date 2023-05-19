package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonNull
public class LessonResult {
    Lesson lesson;
    @NonFinal boolean beenOnALesson = false;

    public void beenOnALesson() {
        beenOnALesson = true;
    }

    public void notBeenOnALesson() {
        beenOnALesson = false;
    }
}
