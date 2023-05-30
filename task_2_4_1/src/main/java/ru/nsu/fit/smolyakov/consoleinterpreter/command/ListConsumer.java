package ru.nsu.fit.smolyakov.consoleinterpreter.command;

import java.util.List;

/**
 * Represents an operation upon list of operands of the same type, producing a result
 * of the same type as the operands.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #execute(List)}.
 *
 * @param <T> a type of operands
 */
@FunctionalInterface
public interface ListConsumer<T> {
    /**
     * Applies this function to the given arguments.
     *
     * @param args a list of specified arguments
     */
    void execute(List<T> args);
}
