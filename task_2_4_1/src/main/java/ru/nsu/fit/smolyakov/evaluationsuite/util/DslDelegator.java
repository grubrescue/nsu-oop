package ru.nsu.fit.smolyakov.evaluationsuite.util;

import groovy.lang.Closure;

public class DslDelegator {
    public static void groovyDelegate (Object delegate, Closure<?> closure) {
        closure.setDelegate(delegate);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }
}
