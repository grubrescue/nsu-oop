package ru.nsu.fit.smolyakov.labchecker.entity.group;

import groovy.lang.Closure;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static ru.nsu.fit.smolyakov.labchecker.util.DSLUtil.groovyDelegate;

@ToString
@Getter
@EqualsAndHashCode
public class Group {
    private final String groupName = "unspecified group name";
    private final StudentsList students = new StudentsList();

    public void students(Closure<?> closure) {
        groovyDelegate(students, closure);
    }

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
}
