package ru.nsu.fit.smolyakov.labchecker.entity.schedule;

import groovy.lang.Closure;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.nsu.fit.smolyakov.labchecker.entity.group.Student;
import ru.nsu.fit.smolyakov.labchecker.util.DSLUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.nsu.fit.smolyakov.labchecker.util.DSLUtil.groovyDelegate;

@Getter
@ToString
@EqualsAndHashCode
public class Schedule {
    @Getter
    @ToString
    @EqualsAndHashCode
    private static class AssignmentsList {
        private final Map<String, Assignment> assignments = new HashMap<>();

        public void assignment(String taskName, Closure<?> closure) {
            Assignment assignment = new Assignment();
            groovyDelegate(assignment, closure);
            assignments.put(taskName, assignment);
        }
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    private static class Lessons {
        private final List<Lesson> lessons = new ArrayList<>();

        public void lesson(String dateString) {
            Lesson lesson = new Lesson(LocalDate.parse(dateString));
            lessons.add(lesson);
        }
    }

    private AssignmentsList assignmentsList = new AssignmentsList();
    private Lessons lessons = new Lessons();

    public void assignments(Closure<?> closure) {
        groovyDelegate(assignmentsList, closure);
    }

    public void lessons(Closure<?> closure) {
        groovyDelegate(lessons, closure);
    }
}
