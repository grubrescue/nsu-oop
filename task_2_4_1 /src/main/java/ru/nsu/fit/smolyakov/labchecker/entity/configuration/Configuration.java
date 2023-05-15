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
    private final Evaluation evaluation = new Evaluation();
    private final Git git = new Git();

    public void evaluation(Closure<?> closure) {
        groovyDelegate(evaluation, closure);
    }

    public void git(Closure<?> closure) {
        groovyDelegate(git, closure);
    }
}
