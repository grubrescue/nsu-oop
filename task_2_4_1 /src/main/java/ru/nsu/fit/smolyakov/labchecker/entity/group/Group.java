package ru.nsu.fit.smolyakov.labchecker.entity.group;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@EqualsAndHashCode(callSuper = false)
public class Group extends GroovyObjectSupport {
    @ToString
    @Getter
    @EqualsAndHashCode(callSuper = false)
    public static class StudentsList {
        private final List<Student> students = new ArrayList<>();

        public void student(Closure<?> closure) {
            Student student = new Student();
            closure.setDelegate(student);
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.call();

            students.add(student);
        }
    }

    private String groupName = "unspecified group name";
    private StudentsList students;

    public void students(Closure<?> closure) {
        students = new StudentsList();
        closure.setDelegate(students);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }
}
