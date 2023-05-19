package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
//@RequiredArgsConstructor
@NonNull
@Builder
public class Student {
    String nickName;
    String fullName;
    String repoUrl;

    @Singular("newAssignment") List<AssignmentStatus> assignmentStatusList;
    @Singular("newLesson") List<LessonStatus> lessonStatusList;

//    private static class StudentBuilder {
//        public StudentBuilder assignmentStatusList(List<AssignmentStatus> assignmentStatusList) {
//            this.assignmentStatusList = new ArrayList<>(assignmentStatusList);
//            return this;
//        }
//
//        public StudentBuilder lessonStatusList(List<LessonStatus> lessonStatusList) {
//            this.lessonStatusList = new ArrayList<>(lessonStatusList);
//            return this;
//        }
//    }
}
