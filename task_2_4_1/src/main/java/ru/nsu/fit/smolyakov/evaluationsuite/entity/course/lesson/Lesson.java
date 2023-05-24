package ru.nsu.fit.smolyakov.evaluationsuite.entity.course.lesson;

import lombok.NonNull;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

@Value
@NonNull
public class Lesson implements Serializable {
    LocalDate date;

    public LessonStatus newLessonStatusInstance(boolean beenOnALesson) {
        return new LessonStatus(this, beenOnALesson);
    }

    public LessonStatus newLessonStatusInstance() {
        return new LessonStatus(this, false);
    }
}
