package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Value
public class Course {
    @Value
    public static class Lessons {
        List<Lesson> lessonList
            = new ArrayList<>(); // TODO

        public Lessons(List<Lesson> lessonList) {
            this.lessonList.addAll(Objects.requireNonNull(lessonList));
        }
   }

    @Value
    public static class Assignments {
        List<Assignment> assignmentList
            = new ArrayList<>();

        public Assignments(List<Assignment> assignmentList) {
            this.assignmentList.addAll(Objects.requireNonNull(assignmentList));
        }
    }

    Lessons lessons;
    Assignments assignments;
}
