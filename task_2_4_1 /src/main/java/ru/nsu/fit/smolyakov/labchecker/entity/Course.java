package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.Value;

import java.util.List;

@Value
public class Course {
    @Value
    public static class Lessons {
        List<Lesson> lessonList; // TODO
    }

    @Value
    public static class Assignments {

    }

//    Lessons lessons = new Lessons();
    Assignments assignments = new Assignments();

}
