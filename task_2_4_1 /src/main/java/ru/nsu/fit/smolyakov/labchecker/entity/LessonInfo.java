package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.Data;
import lombok.Value;
import lombok.With;

@Value
public class LessonInfo {
    Lesson lesson;
    boolean beenOnALesson;
}
