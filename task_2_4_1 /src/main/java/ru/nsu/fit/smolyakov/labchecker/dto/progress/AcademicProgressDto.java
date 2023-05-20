package ru.nsu.fit.smolyakov.labchecker.dto.progress;

import groovy.lang.Closure;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

import static ru.nsu.fit.smolyakov.labchecker.util.DSLUtil.groovyDelegate;

@Value
@NoArgsConstructor
public class AcademicProgressDto {
    OverriddenStudentMap overriddenStudents
        = new OverriddenStudentMap();

    void overrideProgress(Closure<?> closure) {
        groovyDelegate(overriddenStudents, closure);
    }

    @Value
    public class OverriddenStudentMap {
        Map<String, OverriddenStudentDto> map
            = new HashMap<>();

        void forStudent(String nickName, Closure<?> closure) {
            var overrideInfo = new OverriddenStudentDto();
            groovyDelegate(overrideInfo, closure);
            map.put(nickName, overrideInfo);
        }
    }
}
