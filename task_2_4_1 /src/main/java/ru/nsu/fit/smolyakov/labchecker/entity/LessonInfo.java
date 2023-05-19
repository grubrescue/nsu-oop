package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.Data;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import lombok.experimental.NonFinal;

@Value
@NonNull
public class LessonInfo {
    Lesson lesson;
    @NonFinal boolean beenOnALesson = false;

    public void beenOnALesson() {
        beenOnALesson = true;
    }
}
