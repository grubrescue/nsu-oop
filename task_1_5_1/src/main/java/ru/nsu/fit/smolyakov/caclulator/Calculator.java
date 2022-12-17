package ru.nsu.fit.smolyakov.caclulator;

import ru.nsu.fit.smolyakov.caclulator.operation.Operation;
import ru.nsu.fit.smolyakov.caclulator.operationsprovider.OperationsProvider;

import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

/**
 * A calculator for a prefix notation with a generic type of operands and
 * a set of operations provided by {@link OperationsProvider}. As one
 * specifies, operands are also treated as zero arguments functions.
 *
 * @param <T> a type of operands
 * @see ru.nsu.fit.smolyakov.caclulator.operation.Operation
 * @see ru.nsu.fit.smolyakov.caclulator.operationsprovider.OperationsProvider
 */
public class Calculator<T> {
    private final OperationsProvider<T> operationsProvider;

    private final Stack<Operation<T>> stack = new Stack<>();

    /**
     * Constructs an instance of a calculator associated with
     * a specified {@code operationProvider}.
     *
     * @param operationsProvider a specified {@linkplain OperationsProvider}
     */
    public Calculator(OperationsProvider<T> operationsProvider) {
        this.operationsProvider = Objects.requireNonNull(operationsProvider);
    }

    // returns non-null if everything is calculated
    private T curryStackOperations() {
        while (!stack.empty() && stack.peek().arity() == 0) {
            T peekValue = stack.pop().get();

            if (!stack.empty()) {
                stack.peek().curry(peekValue); // TODO catch exception or not?
            } else {
                return peekValue;
            }
        }

        return null;
    }

    /**
     * Calculates an arithmetic expression in a prefix notation
     * provided by {@code scanner}.
     *
     * @param scanner a specified {@linkplain Scanner}
     * @return a result of computation
     * @throws IllegalArgumentException if an expression provided
     *                                  by {@code scanner} has incorrect format
     */
    public T compute(Scanner scanner) {
        if (scanner == null) {
            throw new NullPointerException("scanner is null");
        }

        T result = null;
        stack.clear();

        while (scanner.hasNext() && result == null) {
            String currentWord = scanner.next();
            var operation = operationsProvider.getByName(currentWord);

            stack.push(operation);
            result = curryStackOperations();
        }

        if (scanner.hasNext()) {
            throw new IllegalArgumentException("too many operands");
        } else if (!stack.isEmpty()) {
            throw new IllegalArgumentException("lack of operands");
        } else {
            return result;
        }
    }
}
