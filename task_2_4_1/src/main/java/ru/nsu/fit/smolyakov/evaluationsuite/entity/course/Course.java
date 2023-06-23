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

/**
 * A course.
 */
@Value
public class Course implements Serializable {
    Lessons lessons;
    Assignments assignments;

    /**
     * Lessons.
     */
    @Value
    public static class Lessons implements Serializable {
        List<Lesson> list
            = new ArrayList<>(); // TODO

        /**
         * Creates a new instance with a given list of lessons.
         *
         * @param list a list of lessons
         */
        public Lessons(List<Lesson> list) {
            this.list.addAll(Objects.requireNonNull(list));
        }

        /**
         * Returns a lesson by its date.
         *
         * @param date a date
         * @return {@link Optional} with {@link Lesson} if it exists,
         * {@link Optional#empty()} otherwise
         */
        public Optional<Lesson> getByDate(LocalDate date) {
            return list.stream()
                .filter(lesson -> lesson.getDate().equals(date))
                .findFirst();
        }
    }

    /**
     * Assignments.
     */
    @Value
    public static class Assignments implements Serializable {
        List<Assignment> list
            = new ArrayList<>();

        /**
         * Creates a new instance with a given list of assignments.
         *
         * @param list a list of assignments
         */
        public Assignments(List<Assignment> list) {
            this.list.addAll(Objects.requireNonNull(list));
        }

        /**
         * Returns an assignment by its identifier.
         *
         * @param identifier an identifier
         * @return {@link Optional} with {@link Assignment} if it exists,
         * {@link Optional#empty()} otherwise
         */
        public Optional<Assignment> getByIdentifier(String identifier) {
            return list.stream()
                .filter(assignment -> assignment.getIdentifier().equals(identifier))
                .findFirst();
        }
    }
}
