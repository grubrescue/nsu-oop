package ru.nsu.fit.smolyakov.caclulator.operationsprovider;

import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Just one variant of {@link OperationsProvider} interface implementation,
 * that is responsible for providing operations for
 * {@link ru.nsu.fit.smolyakov.caclulator.Calculator}.
 *
 * <p>As calculator treats everything as a function, it is supposed that
 * operands are returned as suppliers (0-arity functions).
 *
 * <p>Successor classes have to call {@link #AbstractOperationsProvider(Map)}
 * constructor, with a map from method names to {@link Operation} methods.
 * Functionality can be also widened by using {@link #insertOperation(String, Operation)}
 * method. Another requirement for inheritors is to provide an implementation for
 * {@link #parseAsOperand(String)}.
 *
 * <p>This class appears to be mutable, so changing of one leads to changing of behaviour of
 * an associated {@link ru.nsu.fit.smolyakov.caclulator.Calculator}.
 *
 * @param <T> a type of operands
 * @see ru.nsu.fit.smolyakov.caclulator.Calculator
 */
public abstract class AbstractOperationsProvider<T> implements OperationsProvider<T> {

    private final Map<String, Operation<T>> operationsMap;

    /**
     * Constructs an {@code OperationProvider} with mappings from operation name
     * to {@link Operation} instance provided by {@code operationsMap}.
     *
     * @param operationsMap a map from operation name to {@link Operation}.
     */
    protected AbstractOperationsProvider(Map<String, Operation<T>> operationsMap) {
        this.operationsMap = new HashMap<>(Objects.requireNonNull(operationsMap));
    }

    /**
     * Converts {@code operandString} into operand value type ({@code T}).
     *
     * @param operandString a string representation of operand
     * @return an instance of class {@code T}
     * @throws NumberFormatException if {@code operandString} doesn't
     *                               match operand pattern
     */
    protected abstract T parseAsOperand(String operandString) throws NumberFormatException;

    /**
     * Returns a copy of {@link Operation} associated with specified {@code name}.
     * If there is no such operators, then {@code name} is parsed
     * by {@link #parseAsOperand(String)} method.
     *
     * @param name a name of an operation
     * @return an operation associated with {@code name}
     * @throws NumberFormatException if {@code} name is either
     *                               not connected to any operation and doesn't match
     *                               the format of operand
     */
    @Override
    public Operation<T> getByName(String name) throws NumberFormatException {
        var mappedOperation = operationsMap.get(name);
        if (mappedOperation == null) {
            var parsedName = parseAsOperand(name);
            return new Operation<>(() -> parsedName);
        } else {
            return mappedOperation.uncurriedCopy();
        }
    }

    /**
     * Inserts a new {@link Operation} into this {@code OperationsProvider}.
     *
     * <p>A {@code name} is not allowed to match the format of operand.
     *
     * @param name      a specified operation name
     * @param operation a specified operation
     * @return true if operation is added successfully,
     * false if one already exists
     * @throws IllegalArgumentException if operation matches the operand
     *                                  pattern
     */
    public boolean insertOperation(String name, Operation<T> operation) {
        if (name == null) {
            throw new NullPointerException("name can't be null");
        } else if (operation == null) {
            throw new NullPointerException("operation can't be null");
        }

        try {
            parseAsOperand(name);
        } catch (NumberFormatException e) {
            return Objects.isNull(operationsMap.putIfAbsent(name, operation));
        }

        throw new IllegalArgumentException("specified name matches the operands pattern");
    }

    /**
     * Returns an operations map.
     *
     * @return an operations map
     */
    Map<String, Operation<T>> getOperationsMap() {
        return Map.copyOf(operationsMap);
    }
}
