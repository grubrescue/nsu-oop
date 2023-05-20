package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Value
public class Course {
    @Value
    public static class Lessons {
        List<Lesson> list
            = new ArrayList<>(); // TODO

        public Lessons(List<Lesson> list) {
            this.list.addAll(Objects.requireNonNull(list));
        }
   }

    @Value
    public static class Assignments {
        List<Assignment> list
            = new ArrayList<>();

        public Assignments(List<Assignment> list) {
            this.list.addAll(Objects.requireNonNull(list));
        }
    }

    Lessons lessons;
    Assignments assignments;
}
