package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;

public class Course {
    public static class Lessons {
        List<Lesson> lessonList
            = new ArrayList<>(); // TODO
    }

    public static class Assignments {
        List<Task> taskList
            = new ArrayList<>();
    }

    Lessons lessons = new Lessons();
    Assignments assignments = new Assignments();
}
