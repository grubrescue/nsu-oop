package ru.nsu.fit.smolyakov.evaluationsuite.dto.progress;

import groovy.lang.Closure;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static ru.nsu.fit.smolyakov.evaluationsuite.util.DslDelegator.groovyDelegate;

/**
 * This class is used to store data from {@code progress.groovy} (by default)
 * and then to pass it to the entity layer.
 */
@Getter
public class AcademicProgressDto {
    private final OverriddenStudentMap overriddenStudents
        = new OverriddenStudentMap();

    void overrideProgress (Closure<?> closure) {
        groovyDelegate(overriddenStudents, closure);
    }

    @Getter
    public static class OverriddenStudentMap {
        private final Map<String, OverriddenStudentDto> map
            = new HashMap<>();

        void forStudent (String nickName, Closure<?> closure) {
            var overrideInfo = new OverriddenStudentDto();
            groovyDelegate(overrideInfo, closure);
            map.put(nickName, overrideInfo);
        }
    }
}
