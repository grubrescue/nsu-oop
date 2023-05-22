package ru.nsu.fit.smolyakov.labchecker.dto.course;

import groovy.lang.Closure;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

import static ru.nsu.fit.smolyakov.labchecker.util.DSLUtil.groovyDelegate;

@Value
@NoArgsConstructor
public class CourseDto {
    TaskList tasks = new TaskList();

    void tasks(Closure<?> closure) {
        groovyDelegate(tasks, closure);
    }

    @Value
    @NoArgsConstructor
    public static class TaskList {
        List<TaskDto> list = new ArrayList<>();

        void task(Closure<?> closure) {
            TaskDto taskDto = new TaskDto();
            groovyDelegate(taskDto, closure);

            list.add(taskDto);
        }
    }
}
