package ru.nsu.fit.smolyakov.labchecker.util;

import lombok.Value;
import ru.nsu.fit.smolyakov.labchecker.dto.CheckerScriptDto;
import ru.nsu.fit.smolyakov.labchecker.dto.course.TaskDto;
import ru.nsu.fit.smolyakov.labchecker.dto.group.StudentDto;
import ru.nsu.fit.smolyakov.labchecker.dto.progress.OverriddenStudentDto;
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

    private Lesson lessonDtoToEntity(LessonDto lessonDto) {
        return new Lesson(lessonDto.getDate());
    }

    private Assignment taskDtoToEntity(TaskDto taskDto) {
        var scheduleDto = checkerScriptDto.getScheduleDto();
        var configurationDto = checkerScriptDto.getConfigurationDto();

        var assignmentDto = scheduleDto.getAssignments().getMap().get(taskDto.getName());
        if (assignmentDto == null) {
            throw new RuntimeException("No assignment for task " + taskDto.getName()); // TODO custom exceptions
        }

        return Assignment.builder()
            .identifier(taskDto.getName())
            .softDeadline(assignmentDto.getSoftDeadline())
            .hardDeadline(assignmentDto.getHardDeadline())
            .defaultBranch(taskDto.getBranch())
            .softDeadlineSkipFine(configurationDto.getEvaluationDto().getSoftDeadlineSkipFine())
            .hardDeadlineSkipFine(configurationDto.getEvaluationDto().getHardDeadlineSkipFine())
            .maxPoints(
                Optional.ofNullable(taskDto.getPoints())
                    .orElse(configurationDto.getEvaluationDto().getDefaultMaxPoints())
            )
            .solvedPoints(configurationDto.getEvaluationDto().getTaskSolvedPoints())
            .runTests(taskDto.isRunTests())
            .build();
    }

    private Course generateCourseFromDtos() {
        var courseDto = checkerScriptDto.getCourseDto();
        var scheduleDto = checkerScriptDto.getScheduleDto();

        var tasksList = courseDto.getTasks()
            .getList()
            .stream()
            .map(this::taskDtoToEntity)
            .toList(); // СПИСОК ЗАДАЧ

        var lessonList = scheduleDto.getLessons()
            .getList()
            .stream()
            .map(this::lessonDtoToEntity)
            .toList();

        return new Course(
            new Course.Lessons(lessonList),
            new Course.Assignments(tasksList)
        );
    }

    private Student studentDtoToEntity(StudentDto studentDto, Course course) {
        var configurationDto = checkerScriptDto.getConfigurationDto();

        var builder = Student.builder()
            .nickName(studentDto.getNickName())
            .fullName(studentDto.getFullName())
            .docsBranch(configurationDto.getGitDto().getDocsBranch())
            .repoUrl(
                convertToRepoUrl(
                    studentDto.getNickName(),
                    Optional.ofNullable(studentDto.getRepo())
                        .orElse(configurationDto.getGitDto().getDefaultRepoName())
                )
            );

        course.getLessons().getList().forEach(
            lesson -> builder.newLesson(lesson.lessonStatusInstance())
        );

        course.getAssignments().getList().forEach(
            task -> builder.newAssignment(task.assignmentResultInstance())
        );

        return builder.build();
    }

    private Group generateGroupFromDtos(Course course) {
        var groupDto = checkerScriptDto.getGroupDto();

        var studentList = groupDto.getStudents()
            .getList()
            .stream()
            .map(studentDto -> studentDtoToEntity(studentDto, course))
            .toList();

        return new Group(groupDto.getGroupName(), studentList);
    }

    private void tryOverrideLessonStatus(LessonStatus lessonStatus, OverriddenStudentDto overriddenStudentDto) {
        Optional.ofNullable(
            overriddenStudentDto.getBeenOnLessonMap()
                .get(lessonStatus.getLesson().getDate())
        ).ifPresent(lessonStatus::beenOnALesson);
    }

    private void tryOverrideAssignmentStatus(AssignmentStatus assignmentStatus, OverriddenStudentDto overriddenStudentDto) {
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

                    Optional.ofNullable(overriddenTaskInfoDto.getTaskNameAlias())
                        .ifPresent(assignmentStatus::setTaskNameAlias);

                    if (overriddenTaskInfoDto.isNoBranch()) {
                        assignmentStatus.setBranch(null);
                    }
                }
            );
    }

    private void overrideStudentsStatuses(Group group) {
        checkerScriptDto.getAcademicProgressDto()
            .getOverriddenStudents()
            .getMap()
            .forEach((nickName, overriddenStudentDto) -> {
                var student = group.getByNickName(nickName)
                    .orElseThrow(() -> new RuntimeException("No student with nickName " + nickName)); // TODO custom exceptions

                student.getLessonStatusList()
                    .forEach(lessonStatus -> tryOverrideLessonStatus(lessonStatus, overriddenStudentDto));

                student.getAssignmentStatusList()
                    .forEach(assignmentStatus -> tryOverrideAssignmentStatus(assignmentStatus, overriddenStudentDto));
            });
    }


    public MainEntity convert() {
        var course = generateCourseFromDtos();
        var group = generateGroupFromDtos(course);

        overrideStudentsStatuses(group);

        return new MainEntity(course, group);
    }
}
