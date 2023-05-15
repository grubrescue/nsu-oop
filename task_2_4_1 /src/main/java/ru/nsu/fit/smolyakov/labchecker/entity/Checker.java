package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.nsu.fit.smolyakov.labchecker.entity.configuration.Configuration;
import ru.nsu.fit.smolyakov.labchecker.entity.course.Course;
import ru.nsu.fit.smolyakov.labchecker.entity.group.Group;
import ru.nsu.fit.smolyakov.labchecker.entity.progress.AcademicProgress;
import ru.nsu.fit.smolyakov.labchecker.entity.schedule.Schedule;

@Getter
@EqualsAndHashCode
@ToString
public class Checker {
    private Group group = new Group();
    private Schedule schedule = new Schedule();
    private Course course = new Course();
    private Configuration configuration = new Configuration();
//    private AcademicProgress academicProgress = new AcademicProgress();
}
