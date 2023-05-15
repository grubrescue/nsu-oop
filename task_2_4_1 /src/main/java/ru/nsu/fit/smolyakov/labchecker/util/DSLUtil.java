package ru.nsu.fit.smolyakov.labchecker.util;

import groovy.lang.Closure;

public class DSLUtil {
    public static void groovyDelegate(Object delegate, Closure<?> closure) {
        closure.setDelegate(delegate);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }
}
