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
    @Value
    public class OverriddenStudentMap {
        Map<String, OverriddenStudentDto> overrideInfoMap
            = new HashMap<>();

        void forStudent(String nickName, Closure<?> closure) {
            var overrideInfo = new OverriddenStudentDto();
            groovyDelegate(overrideInfo, closure);
            overrideInfoMap.put(nickName, overrideInfo);
        }
    }

    OverriddenStudentMap overriddenStudentMap
        = new OverriddenStudentMap();

    void overrideProgress(Closure<?> closure) {
        groovyDelegate(overriddenStudentMap, closure);
    }
}
