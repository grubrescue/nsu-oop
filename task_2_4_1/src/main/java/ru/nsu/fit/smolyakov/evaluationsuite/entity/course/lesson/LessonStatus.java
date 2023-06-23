package ru.nsu.fit.smolyakov.evaluationsuite.entity.course.lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * Lesson status.
 */
@Data
@NonNull
@AllArgsConstructor
public class LessonStatus implements Serializable {
    private final Lesson lesson;
    private boolean beenOnALesson = false;
}
