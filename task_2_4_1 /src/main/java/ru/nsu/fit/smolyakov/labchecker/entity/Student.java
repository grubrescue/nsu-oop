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

    @Singular("newAssignment") List<AssignmentResult> assignmentResultList;
    @Singular("newLesson") List<LessonResult> lessonResultList;

//    private static class StudentBuilder {
//        public StudentBuilder assignmentResultList(List<AssignmentResult> assignmentResultList) {
//            this.assignmentResultList = new ArrayList<>(assignmentResultList);
//            return this;
//        }
//
//        public StudentBuilder lessonResultList(List<LessonResult> lessonResultList) {
//            this.lessonResultList = new ArrayList<>(lessonResultList);
//            return this;
//        }
//    }
}
