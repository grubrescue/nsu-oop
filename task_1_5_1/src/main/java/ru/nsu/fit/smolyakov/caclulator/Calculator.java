package ru.nsu.fit.smolyakov.caclulator;

import ru.nsu.fit.smolyakov.caclulator.operation.Operation;
import ru.nsu.fit.smolyakov.caclulator.operationsprovider.OperationsProvider;

import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

/**
 * A class providing functionality to
 *
 * @see ru.nsu.fit.smolyakov.caclulator.operation.Operation
 * @see ru.nsu.fit.smolyakov.caclulator.operationsprovider.OperationsProvider
 */
public class Calculator<T> {
    private final OperationsProvider<T> operationsProvider;

    private final Stack<Operation<T>> stack = new Stack<>();

    /**
     * @param operationsProvider
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

    public T compute(Scanner scanner) {
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
