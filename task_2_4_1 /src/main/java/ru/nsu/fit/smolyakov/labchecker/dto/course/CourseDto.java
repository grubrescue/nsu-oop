package ru.nsu.fit.smolyakov.labchecker.dto.course;

import groovy.lang.Closure;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static ru.nsu.fit.smolyakov.labchecker.util.DSLUtil.groovyDelegate;

@Value
@NoArgsConstructor
public class CourseDto {
    TasksList tasks = new TasksList();

    public void tasks(Closure<?> closure) {
        groovyDelegate(tasks, closure);
    }

    @Value
    @NoArgsConstructor
    public static class TasksList {
        List<TaskDto> list = new ArrayList<>();

        public void task(Closure<?> closure) {
            TaskDto taskDto = new TaskDto();
            groovyDelegate(taskDto, closure);

            list.add(taskDto);
        }
    }
}
