package ru.nsu.fit.smolyakov.evaluationsuite.entity.group;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Value
@RequiredArgsConstructor
public class Group implements Serializable {
    String groupName;
    List<Student> studentList = new ArrayList<>();

    public Group(String groupName, List<Student> studentList) {
        this.groupName = groupName;
        this.studentList.addAll(studentList);
    }

    public Optional<Student> getByNickName(String nickName) {
        return studentList.stream()
            .filter(student -> student.getNickName().equals(nickName))
            .findFirst();
    }
}
