package ru.nsu.fit.smolyakov.evaluationsuite.dto.configuration;

import groovy.lang.Closure;
import lombok.Getter;

import static ru.nsu.fit.smolyakov.evaluationsuite.util.DslDelegator.groovyDelegate;

/**
 * This class is used to store data from {@code configuration.groovy} (by default)
 * and then to pass it to the entity layer.
 */
@Getter
public class ConfigurationDto {
    private final EvaluationDto evaluationDto = new EvaluationDto();
    private final GitDto gitDto = new GitDto();

    void evaluation(Closure<?> closure) {
        groovyDelegate(evaluationDto, closure);
    }

    void git(Closure<?> closure) {
        groovyDelegate(gitDto, closure);
    }
}
