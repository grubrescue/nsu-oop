package ru.nsu.fit.smolyakov.evaluationsuite.entity.group;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A group of students.
 */
@Value
@RequiredArgsConstructor
public class Group implements Serializable {
    String groupName;
    List<Student> studentList = new ArrayList<>();

    /**
     * Creates a group with a given name.
     *
     * @param groupName   a group name
     * @param studentList a list of students
     */
    public Group(String groupName, List<Student> studentList) {
        this.groupName = groupName;
        this.studentList.addAll(studentList);
    }

    /**
     * Returns a student by his nickname.
     *
     * @param nickName a student's nickname
     * @return {@link Optional} with {@link Student} if he exists,
     * {@link Optional#empty()} otherwise
     */
    public Optional<Student> getByNickName(String nickName) {
        return studentList.stream()
            .filter(student -> student.getNickName().equals(nickName))
            .findFirst();
    }
}
