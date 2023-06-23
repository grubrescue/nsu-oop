package ru.nsu.fit.smolyakov.evaluationsuite.entity.group;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.assignment.Assignment;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.assignment.AssignmentStatus;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.lesson.Lesson;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.course.lesson.LessonStatus;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * A student.
 */
@Value
@NonNull
@Builder
public class Student implements Serializable {
    String nickName;
    String fullName;
    String repoUrl;
    String defaultBranchName;
    String docsBranch;

    @Singular("newAssignment")
    List<AssignmentStatus> assignmentStatusList;
    @Singular("newLesson")
    List<LessonStatus> lessonStatusList;

    /**
     * Returns {@link AssignmentStatus} by {@link Assignment}.
     *
     * @param assignment an assignment
     * @return {@link Optional} with {@link AssignmentStatus} if it exists,
     * {@link Optional#empty()} otherwise
     */
    public Optional<AssignmentStatus> getAssignmentStatusByAssignment(Assignment assignment) {
        return assignmentStatusList.stream()
            .filter(assignmentStatus -> assignmentStatus.getAssignment().equals(assignment))
            .findFirst();
    }

    /**
     * Returns {@link LessonStatus} by {@link Lesson}.
     *
     * @param lesson a lesson
     * @return {@link Optional} with {@link LessonStatus} if it exists,
     * {@link Optional#empty()} otherwise
     */
    public Optional<LessonStatus> getLessonStatusByLesson(Lesson lesson) {
        return lessonStatusList.stream()
            .filter(lessonStatus -> lessonStatus.getLesson().equals(lesson))
            .findFirst();
    }

    /**
     * Calculates amount of lessons that student has been on.
     *
     * @return amount of lessons that student has been on
     */
    public int calculateAmountOfAttendantLessons() {
        return (int) lessonStatusList.stream()
            .filter(LessonStatus::isBeenOnALesson)
            .count();
    }

    /**
     * Calculates total points that student has for all assignments.
     *
     * @return points that student has for all assignments
     */
    public double calculateTotalPoints() {
        return assignmentStatusList.stream()
            .map(assignmentStatus -> assignmentStatus.getGrade().getResultingPoints())
            .reduce(Double::sum)
            .orElse(0.0);
    }
}
