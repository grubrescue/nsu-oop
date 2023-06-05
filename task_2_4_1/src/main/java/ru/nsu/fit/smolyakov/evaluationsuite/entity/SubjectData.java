package ru.nsu.fit.smolyakov.evaluationsuite.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.Course;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.group.Group;

import java.io.Serializable;
import java.time.ZonedDateTime;

@NonNull
@Getter
@AllArgsConstructor
public class SubjectData implements Serializable { // TODO rename???
    private final Course course;
    private final Group group;

    @Setter
    private ZonedDateTime lastUpdate;
}
