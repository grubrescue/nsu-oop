package ru.nsu.fit.smolyakov.evaluationsuite.util;

import groovy.lang.Closure;

/**
 * Utility class for delegating DSL scripts.
 */
public class DslDelegator {
    /**
     * Delegates a DSL script to the given delegate.
     *
     * @param delegate the delegate
     * @param closure  the DSL script
     */
    public static void groovyDelegate(Object delegate, Closure<?> closure) {
        closure.setDelegate(delegate);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }
}
