package ru.nsu.fit.smolyakov.evaluationsuite.dto;

import lombok.NoArgsConstructor;
import lombok.Value;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.configuration.ConfigurationDto;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.course.CourseDto;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.group.GroupDto;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.progress.AcademicProgressDto;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.schedule.ScheduleDto;

@Value
@NoArgsConstructor
public class SubjectDataDto { // TODO rename // TODO у всех классов менять аннотации а то какой то всратыш уродился
    GroupDto groupDto = new GroupDto();
    ScheduleDto scheduleDto = new ScheduleDto();
    CourseDto courseDto = new CourseDto();
    ConfigurationDto configurationDto = new ConfigurationDto();
    AcademicProgressDto academicProgressDto = new AcademicProgressDto();
}
