package ru.nsu.fit.smolyakov.caclulator.operationsprovider;

import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

/**
 * A provider interface for the {@link ru.nsu.fit.smolyakov.caclulator.Calculator}.
 * Provides only {@link #getByName(String)} method, which returns an 
 * {@link ru.nsu.fit.smolyakov.caclulator.operation.Operation} by its name.
 * 
 * <p>As calculator treats everything as a function, is is supposed that
 * operands are returned as suppliers (0-arity functions).
 */
public interface OperationsProvider<T> {
    public Operation<T> getByName(String name);
}
