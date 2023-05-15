package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.nsu.fit.smolyakov.labchecker.entity.configuration.Configuration;
import ru.nsu.fit.smolyakov.labchecker.entity.course.Course;
import ru.nsu.fit.smolyakov.labchecker.entity.group.Group;
import ru.nsu.fit.smolyakov.labchecker.entity.schedule.Schedule;

@Getter
@EqualsAndHashCode
@ToString
public class Checker {
    private final Group group = new Group();
    private final Schedule schedule = new Schedule();
    private final Course course = new Course();
    private final Configuration configuration = new Configuration();
//    private AcademicProgress academicProgress = new AcademicProgress();
}
