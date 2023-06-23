package ru.nsu.fit.smolyakov.evaluationsuite.entity.course.lesson;

import lombok.NonNull;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Lesson.
 */
@Value
@NonNull
public class Lesson implements Serializable {
    LocalDate date;

    /**
     * Creates a new instance of {@link LessonStatus} corresponding to this lesson.
     *
     * @param beenOnALesson whether the student has been on this lesson
     * @return a new instance of {@link LessonStatus}
     */
    public LessonStatus newLessonStatusInstance(boolean beenOnALesson) {
        return new LessonStatus(this, beenOnALesson);
    }

    /**
     * Creates a new instance of {@link LessonStatus} corresponding to this lesson.
     * The student is considered to have not been on this lesson.
     *
     * @return a new instance of {@link LessonStatus}
     */
    public LessonStatus newLessonStatusInstance() {
        return new LessonStatus(this, false);
    }
}
