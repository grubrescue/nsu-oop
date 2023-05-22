package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
@NonNull
@Builder
public class Student {
    String nickName;
    String fullName;
    String repoUrl;
    String defaultBranchName;
    String docsBranch;

    @Singular("newAssignment")
    List<AssignmentStatus> assignmentStatusList;
    @Singular("newLesson")
    List<LessonStatus> lessonStatusList;

    public Optional<AssignmentStatus> getAssignmentStatusByAssignment(Assignment assignment) {
        return assignmentStatusList.stream()
            .filter(assignmentStatus -> assignmentStatus.getAssignment().equals(assignment))
            .findFirst();
    }

    public Optional<LessonStatus> getLessonStatusByLesson(Lesson lesson) {
        return lessonStatusList.stream()
            .filter(lessonStatus -> lessonStatus.getLesson().equals(lesson))
            .findFirst();
    }
}
