package ru.nsu.fit.smolyakov.evaluationsuite.dto.progress;

import groovy.lang.Closure;
import lombok.Value;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static ru.nsu.fit.smolyakov.evaluationsuite.util.DslDelegator.groovyDelegate;

@Value
public class OverriddenStudentDto {
    Map<LocalDate, Boolean> beenOnLessonMap
        = new HashMap<>();

    Map<String, OverriddenTaskDto> overridenTaskInfoMap
        = new HashMap<>();

    void beenOnLesson(String date) {
        beenOnLessonMap.put(LocalDate.parse(date), true);
    }

    void notBeenOnLesson(String date) {
        beenOnLessonMap.put(LocalDate.parse(date), false);
    }

    void forTask(String taskName, Closure<?> closure) {
        var overriddenTaskInfo = new OverriddenTaskDto(taskName);
        groovyDelegate(overriddenTaskInfo, closure);
        overridenTaskInfoMap.put(taskName, overriddenTaskInfo);
    }
}
