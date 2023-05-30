package ru.nsu.fit.smolyakov.evaluationsuite.util;

import lombok.RequiredArgsConstructor;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.SubjectDataDto;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.course.TaskDto;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.group.StudentDto;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.progress.OverriddenStudentDto;
import ru.nsu.fit.smolyakov.evaluationsuite.dto.schedule.LessonDto;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.SubjectData;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.Course;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.assignment.Assignment;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.assignment.AssignmentStatus;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.lesson.Lesson;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.lesson.LessonStatus;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.group.Group;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.group.Student;

import java.util.Optional;

@RequiredArgsConstructor
public class SubjectDataDtoToEntity {
    private final SubjectDataDto subjectDataDto;

    private String convertToRepoUrl(String nickName, String repoName) {
        var gitDto = subjectDataDto.getConfigurationDto().getGitDto();
        return gitDto.getRepoLinkPrefix() + nickName + "/" + repoName + gitDto.getRepoLinkPostfix();
    }

    private Lesson lessonDtoToEntity(LessonDto lessonDto) {
        return new Lesson(lessonDto.getDate());
    }

    private Assignment taskDtoToEntity(TaskDto taskDto) {
        var scheduleDto = subjectDataDto.getScheduleDto();
        var configurationDto = subjectDataDto.getConfigurationDto();

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
            .jacocoPassCoefficient(
                Optional.ofNullable(
                    configurationDto.getEvaluationDto().getJacocoPassPercentage()
                ).orElse(100)
                    / 100.0
            )
            .solvedPoints(Optional.ofNullable(taskDto.getPoints())
                .orElse(configurationDto.getEvaluationDto().getTaskSolvedPoints()))
            .runTests(taskDto.isRunTests())
            .build();
    }

    private Course generateCourseFromDtos() {
        var courseDto = subjectDataDto.getCourseDto();
        var scheduleDto = subjectDataDto.getScheduleDto();

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
        var configurationDto = subjectDataDto.getConfigurationDto();

        var builder = Student.builder()
            .nickName(studentDto.getNickName())
            .fullName(studentDto.getFullName())
            .docsBranch(configurationDto.getGitDto().getDocsBranch())
            .defaultBranchName(
                Optional.ofNullable(studentDto.getDefaultBranch())
                    .orElse(configurationDto.getGitDto().getDefaultBranch()))
            .repoUrl(
                convertToRepoUrl(
                    studentDto.getNickName(),
                    Optional.ofNullable(studentDto.getRepo())
                        .orElse(configurationDto.getGitDto().getDefaultRepoName())
                )
            );

        course.getLessons().getList().forEach(
            lesson -> builder.newLesson(lesson.newLessonStatusInstance())
        );

        course.getAssignments().getList().forEach(
            task -> builder.newAssignment(task.newAssignmentStatusInstance())
        );

        return builder.build();
    }

    private Group generateGroupFromDtos(Course course) {
        var groupDto = subjectDataDto.getGroupDto();

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
        ).ifPresent(lessonStatus::setBeenOnALesson);
    }

    private void tryOverrideAssignmentStatus(AssignmentStatus assignmentStatus, OverriddenStudentDto overriddenStudentDto) {
        Optional.ofNullable(
                overriddenStudentDto.getOverridenTaskInfoMap()
                    .get(assignmentStatus.getAssignment().getIdentifier()))
            .ifPresent(
                overriddenTaskInfoDto -> {
                    Optional.ofNullable(overriddenTaskInfoDto.getPoints())
                        .ifPresent(newTaskPoints -> assignmentStatus.getGrade().overrideTaskPoints(newTaskPoints));

                    Optional.ofNullable(overriddenTaskInfoDto.getBranch())
                        .ifPresent(assignmentStatus::setBranch);

                    Optional.ofNullable(overriddenTaskInfoDto.getStarted())
                        .ifPresent(newStarted -> assignmentStatus.getPass().setStarted(newStarted));

                    Optional.ofNullable(overriddenTaskInfoDto.getFinished())
                        .ifPresent(newFinished -> assignmentStatus.getPass().setFinished(newFinished));

                    Optional.ofNullable(overriddenTaskInfoDto.getMessage())
                        .ifPresent(assignmentStatus::setMessage);

                    Optional.ofNullable(overriddenTaskInfoDto.getIdentifierAlias())
                        .ifPresent(assignmentStatus::setIdentifierAlias);

                    if (overriddenTaskInfoDto.isNoBranch()) {
                        assignmentStatus.setBranch(null);
                    }
                }
            );
    }

    private void overrideStudentsStatuses(Group group) {
        subjectDataDto.getAcademicProgressDto()
            .getOverriddenStudents()
            .getMap()
            .forEach((nickName, overriddenStudentDto) -> {
                    group.getByNickName(nickName)
                        .ifPresent(student -> {
                                student.getLessonStatusList()
                                    .forEach(lessonStatus -> tryOverrideLessonStatus(lessonStatus, overriddenStudentDto));

                                student.getAssignmentStatusList()
                                    .forEach(assignmentStatus -> tryOverrideAssignmentStatus(assignmentStatus, overriddenStudentDto));
                            }
                        );
                }
            );
    }

    public SubjectData convert() {
        var course = generateCourseFromDtos();
        var group = generateGroupFromDtos(course);

        overrideStudentsStatuses(group);

        return new SubjectData(course, group);
    }
}
