package ru.nsu.fit.smolyakov.evaluationsuite.dto;

import lombok.Getter;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.configuration.ConfigurationDto;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.course.CourseDto;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.group.GroupDto;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.progress.AcademicProgressDto;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.schedule.ScheduleDto;

/**
 * A main DTO class, each field of which is a DTO, representing corresponding Groovy DSL block.
 */
@Getter
public class SubjectDataDto { // TODO rename // TODO у всех классов менять аннотации а то какой то всратыш уродился
    private final GroupDto groupDto = new GroupDto();
    private final ScheduleDto scheduleDto = new ScheduleDto();
    private final CourseDto courseDto = new CourseDto();
    private final ConfigurationDto configurationDto = new ConfigurationDto();
    private final AcademicProgressDto academicProgressDto = new AcademicProgressDto();
}
