package ru.nsu.fit.smolyakov.evaluationsuite.dto.configuration;

import groovy.lang.Closure;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

import static ru.nsu.fit.smolyakov.evaluationsuite.util.DslDelegator.groovyDelegate;

@Value
@NoArgsConstructor
public class ConfigurationDto {
    @NonFinal
    EvaluationDto evaluationDto = new EvaluationDto();
    @NonFinal
    GitDto gitDto = new GitDto();

    void evaluation(Closure<?> closure) {
        groovyDelegate(evaluationDto, closure);
    }

    void git(Closure<?> closure) {
        groovyDelegate(gitDto, closure);
    }
}
