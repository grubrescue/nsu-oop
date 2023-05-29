package ru.nsu.fit.smolyakov.evaluationsuite.dto.course;

import groovy.lang.Closure;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static ru.nsu.fit.smolyakov.evaluationsuite.util.DslDelegator.groovyDelegate;

/**
 * This class is used to store data from {@code course.groovy} (by default)
 * and then to pass it to the entity layer.
 */
@Getter
public class CourseDto {
    private final TaskList tasks = new TaskList();

    void tasks (Closure<?> closure) {
        groovyDelegate(tasks, closure);
    }

    @Getter
    public static class TaskList {
        private final List<TaskDto> list = new ArrayList<>();

        void task (Closure<?> closure) {
            TaskDto taskDto = new TaskDto();
            groovyDelegate(taskDto, closure);

            list.add(taskDto);
        }
    }
}
