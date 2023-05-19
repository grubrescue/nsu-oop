package ru.nsu.fit.smolyakov.labchecker.util;

import lombok.Value;
import ru.nsu.fit.smolyakov.labchecker.dto.CheckerScriptDto;
import ru.nsu.fit.smolyakov.labchecker.dto.configuration.EvaluationDto;
import ru.nsu.fit.smolyakov.labchecker.dto.group.StudentDto;
import ru.nsu.fit.smolyakov.labchecker.dto.schedule.LessonDto;
import ru.nsu.fit.smolyakov.labchecker.entity.Course;
import ru.nsu.fit.smolyakov.labchecker.entity.Lesson;
import ru.nsu.fit.smolyakov.labchecker.entity.MainEntity;
import ru.nsu.fit.smolyakov.labchecker.entity.Student;

@Value
public class DtoToEntity {
    CheckerScriptDto checkerScriptDto;

    public MainEntity convert() {
        var courseDto = checkerScriptDto.getCourseDto();
        var groupDto = checkerScriptDto.getGroupDto();
        var scheduleDto = checkerScriptDto.getScheduleDto();
        var configurationDto = checkerScriptDto.getConfigurationDto();
        var academicProgressDto = checkerScriptDto.getAcademicProgressDto();
    }

    private Course.Evaluation evaluationDtoToEntity(EvaluationDto evaluationDto) {
        return new Course.Evaluation(
            evaluationDto.getDefaultMaxPoints(),
            evaluationDto.getTaskSolvedPoints(),
            evaluationDto.getSoftDeadlineSkipFine(),
            evaluationDto.getHardDeadlineSkipFine()
        );
    }

    private Lesson lessonDtoToEntity(LessonDto lessonDto) {
        return new Lesson(lessonDto.getDate());
    }

    private Student studentDtoToEntity(StudentDto studentDto) {
        return new Student(

        );
    }


}
