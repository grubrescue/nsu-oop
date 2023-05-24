package ru.nsu.fit.smolyakov.labchecker.entity.course;

import lombok.Value;
import ru.nsu.fit.smolyakov.labchecker.entity.course.assignment.Assignment;
import ru.nsu.fit.smolyakov.labchecker.entity.course.lesson.Lesson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Value
public class Course {
    Lessons lessons;
    Assignments assignments;

    @Value
    public static class Lessons {
        List<Lesson> list
            = new ArrayList<>(); // TODO

        public Lessons(List<Lesson> list) {
            this.list.addAll(Objects.requireNonNull(list));
        }

        public Optional<Lesson> getByDate(LocalDate date) {
            return list.stream()
                .filter(lesson -> lesson.getDate().equals(date))
                .findFirst();
        }
    }

    @Value
    public static class Assignments {
        List<Assignment> list
            = new ArrayList<>();

        public Assignments(List<Assignment> list) {
            this.list.addAll(Objects.requireNonNull(list));
        }

        public Optional<Assignment> getByIdentifier(String identifier) {
            return list.stream()
                .filter(assignment -> assignment.getIdentifier().equals(identifier))
                .findFirst();
        }
    }
}
