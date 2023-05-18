package ru.nsu.fit.smolyakov.labchecker.dto.configuration;

import groovy.lang.Closure;
import lombok.*;
import lombok.experimental.NonFinal;

import static ru.nsu.fit.smolyakov.labchecker.util.DSLUtil.groovyDelegate;

@Value
@NoArgsConstructor
public class ConfigurationDto {
    @NonFinal EvaluationDto evaluationDto = new EvaluationDto();
    @NonFinal GitDto gitDto = new GitDto();

    void evaluation(Closure<?> closure) {
        groovyDelegate(evaluationDto, closure);
    }

    void git(Closure<?> closure) {
        groovyDelegate(gitDto, closure);
    }
}
