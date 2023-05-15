package ru.nsu.fit.smolyakov.labchecker.entity.configuration;

import groovy.lang.Closure;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static ru.nsu.fit.smolyakov.labchecker.util.DSLUtil.groovyDelegate;

@Getter
@EqualsAndHashCode
@ToString
public class Configuration {
    private Evaluation evaluation = new Evaluation();
    private Git git = new Git();

    public void evaluation(Closure<?> closure) {
        groovyDelegate(evaluation, closure);
    }
}
