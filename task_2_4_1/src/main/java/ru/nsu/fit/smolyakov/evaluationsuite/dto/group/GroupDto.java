package ru.nsu.fit.smolyakov.evaluationsuite.dto.group;

import groovy.lang.Closure;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static ru.nsu.fit.smolyakov.evaluationsuite.util.DslDelegator.groovyDelegate;

/**
 * This class is used to store data from {@code group.groovy} (by default)
 * and then to pass it to the entity layer.
 */
@Getter
public class GroupDto {
    private final StudentList students = new StudentList();
    private final String groupName = "unspecified group name";

    void students(Closure<?> closure) {
        groovyDelegate(students, closure);
    }

    @Getter
    public static class StudentList {
        private final List<StudentDto> list = new ArrayList<>();

        void student(String nickName, Closure<?> closure) {
            StudentDto studentDto = new StudentDto(nickName);
            groovyDelegate(studentDto, closure);

            list.add(studentDto);
        }
    }
}
