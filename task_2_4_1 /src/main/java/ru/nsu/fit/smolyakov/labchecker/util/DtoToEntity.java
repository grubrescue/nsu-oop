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
        // TODO НЕТ ЭТО ПРЯМ НАДО ОТРЕФАКТОРИТЬ
        // TODO НУ ПРЯМ ОЧЕНЬ НАДО!!!
        // TODO разбить на методы???
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
        var studentList = groupDto.getStudents()
            .getList()
            .stream()
            .map(studentDto -> {
                var builder = Student.builder()
                    .nickName(studentDto.getNickName())
                    .fullName(studentDto.getFullName())
                    .repoUrl(convertToRepoUrl(studentDto.getNickName(), studentDto.getRepo()));

                lessonList.forEach(
                    lesson -> builder.newLesson(lesson.lessonStatusInstance())
                );

                tasksList.forEach(
                    task -> builder.newAssignment(task.assignmentResultInstance())
                );

                return builder.build();
            }
            )
            .toList();

        var group = new Group(groupDto.getGroupName(), studentList);

        academicProgressDto
            .getOverriddenStudents()
            .getMap()
            .forEach((nickName, overriddenStudentDto) -> {
                var student = group.getByNickName(nickName)
                    .orElseThrow(() -> new RuntimeException("No student with nickName " + nickName)); // TODO custom exceptions

                student.getLessonStatusList()
                    .forEach(
                        lessonStatus -> {
                            Optional.ofNullable(
                                overriddenStudentDto.getBeenOnLessonMap()
                                    .get(lessonStatus.getLesson().getDate())
                                ).ifPresent(lessonStatus::beenOnALesson);
                        }
                    );

                student.getAssignmentStatusList()
                    .forEach(
                        assignmentStatus -> {
                            Optional.ofNullable(
                                overriddenStudentDto.getOverridenTaskInfoMap()
                                    .get(assignmentStatus.getAssignment().getIdentifier()))
                                .ifPresent(
                                    overriddenTaskInfoDto -> {
                                        Optional.ofNullable(overriddenTaskInfoDto.getPoints())
                                            .ifPresent(assignmentStatus::overrideTaskPoints);

                                        Optional.ofNullable(overriddenTaskInfoDto.getBranch())
                                            .ifPresent(assignmentStatus::setBranch);

                                        Optional.ofNullable(overriddenTaskInfoDto.getStarted())
                                            .ifPresent(assignmentStatus::setStarted);

                                        Optional.ofNullable(overriddenTaskInfoDto.getFinished())
                                            .ifPresent(assignmentStatus::setFinished);

                                        Optional.ofNullable(overriddenTaskInfoDto.getMessage())
                                            .ifPresent(assignmentStatus::setMessage);
                                        }
                                );
                        }
                        );
            });

        return null; // TODO tmp
    }


}
