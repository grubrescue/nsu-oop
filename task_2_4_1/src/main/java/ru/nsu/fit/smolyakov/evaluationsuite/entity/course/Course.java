package ru.nsu.fit.smolyakov.evaluationsuite.entity.course;

import lombok.Value;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.assignment.Assignment;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.lesson.Lesson;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Value
public class Course implements Serializable {
    Lessons lessons;
    Assignments assignments;

    @Value
    public static class Lessons implements Serializable {
        List<Lesson> list
            = new ArrayList<>(); // TODO

        public Lessons (List<Lesson> list) {
            this.list.addAll(Objects.requireNonNull(list));
        }

        public Optional<Lesson> getByDate (LocalDate date) {
            return list.stream()
                .filter(lesson -> lesson.getDate().equals(date))
                .findFirst();
        }
    }

    @Value
    public static class Assignments implements Serializable {
        List<Assignment> list
            = new ArrayList<>();

        public Assignments (List<Assignment> list) {
            this.list.addAll(Objects.requireNonNull(list));
        }

        public Optional<Assignment> getByIdentifier (String identifier) {
            return list.stream()
                .filter(assignment -> assignment.getIdentifier().equals(identifier))
                .findFirst();
        }
    }
}
