package ru.nsu.fit.smolyakov.labchecker.entity.course;

import groovy.lang.Closure;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static ru.nsu.fit.smolyakov.labchecker.util.DSLUtil.groovyDelegate;

@Getter
@EqualsAndHashCode
@ToString
public class Course {
    private final TasksList tasks = new TasksList();

    public void tasks(Closure<?> closure) {
        groovyDelegate(tasks, closure);
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    public static class TasksList {
        private final List<Task> list = new ArrayList<>();

        public void task(Closure<?> closure) {
            Task task = new Task();
            groovyDelegate(task, closure);

            list.add(task);
        }
    }
}
