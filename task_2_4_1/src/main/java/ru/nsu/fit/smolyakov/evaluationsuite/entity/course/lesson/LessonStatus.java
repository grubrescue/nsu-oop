package ru.nsu.fit.smolyakov.evaluationsuite.entity.course.lesson;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.io.Serializable;

@Value
@NonNull
public class LessonStatus implements Serializable {
    Lesson lesson;
    @NonFinal
    boolean beenOnALesson = false;

    public void setBeenOnALesson (boolean value) {
        beenOnALesson = value;
    }
}
