package ru.nsu.fit.smolyakov.evaluationsuite.entity;

import lombok.NonNull;
import lombok.Value;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.Course;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.group.Group;

import java.io.Serializable;

@NonNull
@Value
public class SubjectData implements Serializable { // TODO rename???
    Course course;
    Group group;
}
