package ru.nsu.fit.smolyakov.caclulator.operationsprovider;

import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

/**
 * An interface responsible for providing operations for
 * {@link ru.nsu.fit.smolyakov.caclulator.Calculator}.
 * Contains only {@link #getByName(String)} method, which returns an
 * {@link Operation} by its name.
 *
 * <p>As calculator treats everything as a function, it is supposed that
 * operands are returned as suppliers (0-arity functions).
 *
 * @param <T> a type of operands
 * @see ru.nsu.fit.smolyakov.caclulator.operation.Function
 * @see Operation
 */
public interface OperationsProvider<T> {
    /**
     * Returns an {@link Operation} associated with specified {@code name}.
     * If there is no such operators, then {@code name} is parsed
     * according to operand pattern.
     *
     * @param  name a name of an operation
     * @return an operation associated with {@code name}
     * @throws NumberFormatException if {@code} name is either
     *      not connected to any operation and doesn't match
     *      the format of operand
     */
    Operation<T> getByName(String name);
}
