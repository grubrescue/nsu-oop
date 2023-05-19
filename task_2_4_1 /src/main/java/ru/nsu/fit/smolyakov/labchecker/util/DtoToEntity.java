package ru.nsu.fit.smolyakov.labchecker.util;

import lombok.Value;
import ru.nsu.fit.smolyakov.labchecker.dto.CheckerScriptDto;
import ru.nsu.fit.smolyakov.labchecker.dto.schedule.LessonDto;
import ru.nsu.fit.smolyakov.labchecker.entity.Course;
import ru.nsu.fit.smolyakov.labchecker.entity.Lesson;
import ru.nsu.fit.smolyakov.labchecker.entity.MainEntity;
import ru.nsu.fit.smolyakov.labchecker.entity.Task;

import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Value
public class DtoToEntity {
    CheckerScriptDto checkerScriptDto;

    private String repoUrl(String nickName, String repoName) {
        var gitDto = checkerScriptDto.getConfigurationDto().getGitDto();
        return gitDto.getRepoLinkPrefix() + nickName + "/" + repoName + gitDto.getRepoLinkPostfix();
    }

    private static Lesson lessonDtoToEntity(LessonDto lessonDto) {
        return new Lesson(lessonDto.getDate());
    }


    public MainEntity convert() {
        var courseDto = checkerScriptDto.getCourseDto();
        var groupDto = checkerScriptDto.getGroupDto();
        var scheduleDto = checkerScriptDto.getScheduleDto();
        var configurationDto = checkerScriptDto.getConfigurationDto();
        var academicProgressDto = checkerScriptDto.getAcademicProgressDto();

        Course.Lessons lessons = new Course.Lessons()
        var course = new Course();
        MainEntity mainEntity = new MainEntity();

        var tasksList = courseDto.getTasks()
            .getList()
            .stream()
            .map(taskDto -> {
                var assignmentDto = scheduleDto.getAssignments().getMap().get(taskDto.getName());
                if (assignmentDto == null) {
                    throw new RuntimeException("No assignment for task " + taskDto.getName()); // TODO custom exceptions
                }

                return Task.builder()
                    .identifier(taskDto.getName())
                    .softDeadline(assignmentDto.getSoftDeadline())
                    .hardDeadline(assignmentDto.getHardDeadline())
                    .defaultBranch(taskDto.getBranch())
                    .softDeadlineSkipFine(
                        configurationDto.getEvaluationDto().getSoftDeadlineSkipFine()
                    )
                    .hardDeadlineSkipFine(
                        configurationDto.getEvaluationDto().getHardDeadlineSkipFine()
                    )
                    .maxPoints(
                        Optional.ofNullable(taskDto.getPoints())
                            .orElse(configurationDto.getEvaluationDto().getDefaultMaxPoints())
                    )
                    .solvedPoints(
                        configurationDto.getEvaluationDto().getTaskSolvedPoints()
                    )
                    .runTests(taskDto.isRunTests())
                    .build();
            })
            .toList(); // СПИСОК ЗАДАЧ


        scheduleDto.getLessons()
            .getList()
            .stream()
            .map(DtoToEntity::lessonDtoToEntity)
            .toList(); // СПИСОК УРОКОВ. ПОКА ЧТО НЕ ОВВЕРРАЙДИЛ БЫЛ НА УРОКЕ ИЛИ НЕТ. КАК ТО ТАК







        return null; // TODO tmp
    }


}
