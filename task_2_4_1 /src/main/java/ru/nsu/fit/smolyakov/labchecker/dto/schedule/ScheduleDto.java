package ru.nsu.fit.smolyakov.labchecker.dto.schedule;

import groovy.lang.Closure;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.nsu.fit.smolyakov.labchecker.util.DSLUtil.groovyDelegate;

@Value
public class ScheduleDto {
    AssignmentMap assignments = new AssignmentMap();
    Lessons lessons = new Lessons();

    void assignments(Closure<?> closure) {
        groovyDelegate(assignments, closure);
    }

    void lessons(Closure<?> closure) {
        groovyDelegate(lessons, closure);
    }

    @Value
    public static class AssignmentMap {
        Map<String, AssignmentDto> map = new HashMap<>();

        void assignment(String taskName, Closure<?> closure) {
            AssignmentDto assignmentDto = new AssignmentDto(taskName);
            groovyDelegate(assignmentDto, closure);
            map.put(taskName, assignmentDto);
        }
    }

    @Value
    public static class Lessons {
        List<LessonDto> list = new ArrayList<>();

        void lessonAt(String dateString) {
            LessonDto lessonDto = new LessonDto(LocalDate.parse(dateString));
            list.add(lessonDto);
        }
    }
}
