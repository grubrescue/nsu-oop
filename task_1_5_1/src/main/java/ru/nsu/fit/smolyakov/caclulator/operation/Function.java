package ru.nsu.fit.smolyakov.caclulator.operation;

import java.util.List;

/**
 * Represents an operation upon list of operands of the same type, producing a result
 * of the same type as the operands.
 * 
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(List)}.
 */
public interface Function<T> {
    /**
     * Applies this function to the given arguments.
     * 
     * @param  args  a list of specified arguments
     * @return a result of an application
     */
    public T apply(List<T> args);
}
