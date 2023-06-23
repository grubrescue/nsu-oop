package ru.nsu.fit.smolyakov.evaluationsuite.dto.schedule;

import groovy.lang.Closure;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.nsu.fit.smolyakov.evaluationsuite.util.DslDelegator.groovyDelegate;

/**
 * This class is used to store data from {@code schedule.groovy} (by default)
 * and then to pass it to the entity layer.
 */
@Getter
public class ScheduleDto {
    private final AssignmentMap assignments = new AssignmentMap();
    private final Lessons lessons = new Lessons();

    void assignments(Closure<?> closure) {
        groovyDelegate(assignments, closure);
    }

    void lessons(Closure<?> closure) {
        groovyDelegate(lessons, closure);
    }

    @Getter
    public static class AssignmentMap {
        private final Map<String, AssignmentDto> map = new HashMap<>();

        void assignment(String taskName, Closure<?> closure) {
            AssignmentDto assignmentDto = new AssignmentDto(taskName);
            groovyDelegate(assignmentDto, closure);
            map.put(taskName, assignmentDto);
        }
    }

    @Getter
    public static class Lessons {
        private final List<LessonDto> list = new ArrayList<>();

        void lessonAt(String dateString) {
            LessonDto lessonDto = new LessonDto(LocalDate.parse(dateString));
            list.add(lessonDto);
        }
    }
}
