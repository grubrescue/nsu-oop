package ru.nsu.fit.smolyakov.caclulator;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.Stack;

import ru.nsu.fit.smolyakov.caclulator.exceptions.IllegalOperationsAmountException;
import ru.nsu.fit.smolyakov.caclulator.exceptions.UnknownOperationException;
import ru.nsu.fit.smolyakov.caclulator.operation.Operation;
import ru.nsu.fit.smolyakov.caclulator.operationsprovider.OperationsProvider;

/**
 * A calculator for a prefix notation with a generic type of operands and
 * a set of operations provided by {@link OperationsProvider}. As one
 * specifies, operands are also treated as zero arguments functions.
 *
 * <p>The main calculating methods are {@link #compute(Scanner)} and
 * {@link #compute(InputStream)} alias. Throws inheritors of
 * {@link ru.nsu.fit.smolyakov.caclulator.exceptions.CalculatorException}
 * if input expression is incorrect.
 *
 * <p>Allows to be extended by {@link #insertProvider(OperationsProvider)},
 * Detailed information of providers priority is contained in the description
 * of that method.
 *
 * @param <T> a type of operands
 * @see ru.nsu.fit.smolyakov.caclulator.operation.Operation
 * @see ru.nsu.fit.smolyakov.caclulator.operationsprovider.OperationsProvider
 */
public class Calculator<T> {
    private final List<OperationsProvider<T>> operationsProviderList
        = new LinkedList<>();

    private final Stack<Operation<T>> stack = new Stack<>();

    /**
     * Constructs an instance of a calculator associated with
     * a specified {@code operationProvider}.
     *
     * @param operationsProvider a specified {@linkplain OperationsProvider}
     */
    public Calculator(OperationsProvider<T> operationsProvider) {
        insertProvider(operationsProvider);
    }

    /**
     * Inserts an {@link OperationsProvider} in this {@code Calculator}.
     *
     * <p>As an order of providers is important, specified {@code operationsProvider}
     * has higher priority than providers inserted earlier. So, this provider's
     * lexemes overrides ones previously defined.
     *
     * @param operationsProvider a provider to insert
     */
    public void insertProvider(OperationsProvider<T> operationsProvider) {
        this.operationsProviderList.add(0, Objects.requireNonNull(operationsProvider));
    }

    // returns non-null if everything is calculated
    private T curryStackOperations() {
        while (!stack.empty() && stack.peek().arity() == 0) {
            T peekValue = stack.pop().get();

            if (!stack.empty()) {
                stack.peek().curry(peekValue);
            } else {
                return peekValue;
            }
        }

        return null;
    }

    /**
     * Calculates an arithmetic expression in a prefix notation
     * provided by {@code inputStream}.
     *
     * @param inputStream a specified {@linkplain InputStream}
     * @return a result of computation
     * @throws IllegalArgumentException if an expression provided
     *                                  by {@code inputStream} has incorrect format
     */
    public T compute(InputStream inputStream) {
        return compute(new Scanner(Objects.requireNonNull(inputStream)));
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

            Optional<Operation<T>> operation =
                Optional.empty();

            for (var provider : operationsProviderList) {
                operation = provider.getOptionalByName(currentWord);
                if (operation.isPresent()) {
                    break;
                }
            }

            if (operation.isEmpty()) {
                throw new UnknownOperationException();
            }

            stack.push(operation.get());
            result = curryStackOperations();
        }

        if (scanner.hasNext()) {
            throw new IllegalOperationsAmountException("too many tokens");
        } else if (!stack.isEmpty()) {
            throw new IllegalOperationsAmountException("lack of tokens");
        } else {
            return result;
        }
    }
}
