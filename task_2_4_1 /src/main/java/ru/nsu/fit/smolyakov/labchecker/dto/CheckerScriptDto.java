package ru.nsu.fit.smolyakov.labchecker.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.nsu.fit.smolyakov.labchecker.dto.configuration.ConfigurationDto;
import ru.nsu.fit.smolyakov.labchecker.dto.course.CourseDto;
import ru.nsu.fit.smolyakov.labchecker.dto.group.GroupDto;
import ru.nsu.fit.smolyakov.labchecker.dto.schedule.ScheduleDto;

@Getter
@EqualsAndHashCode
@ToString
public class CheckerScriptDto { // TODO rename
    private final GroupDto groupDto = new GroupDto();
    private final ScheduleDto scheduleDto = new ScheduleDto();
    private final CourseDto courseDto = new CourseDto();
    private final ConfigurationDto configuration = new ConfigurationDto();
//    private AcademicProgress academicProgress = new AcademicProgress();
}
