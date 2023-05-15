package ru.nsu.fit.smolyakov.labchecker.entity.group;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.nsu.fit.smolyakov.labchecker.util.DSLUtil.groovyDelegate;

@ToString
@Getter
@EqualsAndHashCode
public class Group {
    @ToString
    @Getter
    @EqualsAndHashCode
    public static class StudentsList {
        private final List<Student> students = new ArrayList<>();

        public void student(Closure<?> closure) {
            Student student = new Student();
            groovyDelegate(student, closure);

            students.add(student);
        }
    }

    private String groupName = "unspecified group name";
    private StudentsList students = new StudentsList();

    public void students(Closure<?> closure) {
        groovyDelegate(students, closure);
    }
}
