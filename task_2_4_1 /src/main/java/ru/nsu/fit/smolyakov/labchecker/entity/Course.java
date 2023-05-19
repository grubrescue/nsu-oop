package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
public class Course {
    @Value
    public static class Lessons {
        List<Lesson> lessonList
            = new ArrayList<>(); // TODO
   }

    @Value
    public static class Assignments {
        List<Task> taskList
            = new ArrayList<>();
    }

    Lessons lessons = new Lessons();
    Assignments assignments = new Assignments();
}
