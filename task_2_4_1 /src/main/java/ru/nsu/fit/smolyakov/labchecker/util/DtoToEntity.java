package ru.nsu.fit.smolyakov.labchecker.util;

import lombok.Value;
import ru.nsu.fit.smolyakov.labchecker.dto.CheckerScriptDto;
import ru.nsu.fit.smolyakov.labchecker.dto.schedule.LessonDto;
import ru.nsu.fit.smolyakov.labchecker.entity.*;

import java.util.Optional;

@Value
public class DtoToEntity {
    CheckerScriptDto checkerScriptDto;

    private String convertToRepoUrl(String nickName, String repoName) {
        var gitDto = checkerScriptDto.getConfigurationDto().getGitDto();
        return gitDto.getRepoLinkPrefix() + nickName + "/" + repoName + gitDto.getRepoLinkPostfix();
    }

    private static Lesson lessonDtoToEntity(LessonDto lessonDto) {
        return new Lesson(lessonDto.getDate());
    }

    public MainEntity convert() { // TODO refactor
        var courseDto = checkerScriptDto.getCourseDto();
        var groupDto = checkerScriptDto.getGroupDto();
        var scheduleDto = checkerScriptDto.getScheduleDto();
        var configurationDto = checkerScriptDto.getConfigurationDto();
        var academicProgressDto = checkerScriptDto.getAcademicProgressDto();

        var tasksList = courseDto.getTasks()
            .getList()
            .stream()
            .map(taskDto -> {
                var assignmentDto = scheduleDto.getAssignments().getMap().get(taskDto.getName());
                if (assignmentDto == null) {
                    throw new RuntimeException("No assignment for task " + taskDto.getName()); // TODO custom exceptions
                }

                return Assignment.builder()
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

        var lessonList = scheduleDto.getLessons()
            .getList()
            .stream()
            .map(DtoToEntity::lessonDtoToEntity)
            .toList();

        var course = new Course(
            new Course.Lessons(lessonList),
            new Course.Assignments(tasksList)
        );

        System.out.println(course);

//        var group = new Group()
        var a = groupDto.getStudents()
            .getList()
            .stream()
            .map(studentDto -> {
                var builder = Student.builder()
                    .nickName(studentDto.getNickName())
                    .fullName(studentDto.getFullName())
                    .repoUrl(convertToRepoUrl(studentDto.getNickName(), studentDto.getRepo()));

                lessonList.forEach(
                    lesson -> builder.newLesson(lesson.lessonResultInstance())
                );

                tasksList.forEach(
                    task -> builder.newAssignment(task.assignmentResultInstance())
                );

                return builder.build();
            }
            );


        return null; // TODO tmp
    }


}
