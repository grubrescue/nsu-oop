package ru.nsu.fit.smolyakov.labchecker.dto.group;

import groovy.lang.Closure;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.ArrayList;
import java.util.List;

import static ru.nsu.fit.smolyakov.labchecker.util.DSLUtil.groovyDelegate;

@Value
@NoArgsConstructor
public class GroupDto {
    StudentList students = new StudentList();
    @NonFinal
    String groupName = "unspecified group name";

    void students(Closure<?> closure) {
        groovyDelegate(students, closure);
    }

    @Value
    @NoArgsConstructor
    public static class StudentList {
        List<StudentDto> list = new ArrayList<>();

        void student(String nickName, Closure<?> closure) {
            StudentDto studentDto = new StudentDto(nickName);
            groovyDelegate(studentDto, closure);

            list.add(studentDto);
        }
    }
}
