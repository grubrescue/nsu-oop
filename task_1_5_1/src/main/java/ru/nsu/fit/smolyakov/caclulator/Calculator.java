package ru.nsu.fit.smolyakov.caclulator;

import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.Stack;

import ru.nsu.fit.smolyakov.caclulator.operandparser.OperandParser;
import ru.nsu.fit.smolyakov.caclulator.operation.Operation;
import ru.nsu.fit.smolyakov.caclulator.operationsprovider.OperationsProvider;

/**
 * A class providing functionality to 
 * 
 * @see ru.nsu.fit.smolyakov.caclulator.operation.Operation
 * @see ru.nsu.fit.smolyakov.caclulator.operationsprovider.OperationsProvider
 * @see ru.nsu.fit.smolyakov.caclulator.operandparser.OperandParser
 */
public class Calculator<T> {
    private OperationsProvider<T> operationsProvider;
    private OperandParser<T> operandParser;

    private Stack<Operation<T>> stack = new Stack<>();
    private int maxArity = 1;

    /**
     * 
     * @param operationsProvider
     * @param operandParser
     * 
     */
    public Calculator(OperationsProvider<T> operationsProvider, 
                      OperandParser<T> operandParser) {
        this.operationsProvider = Objects.requireNonNull(operationsProvider);
        this.operandParser = Objects.requireNonNull(operandParser);
    }

    private String parseOperations(Scanner scanner) {
        while (scanner.hasNext()) {
            String operationString = scanner.next();
            Optional<Operation<T>> operation = operationsProvider.getByName(operationString);

            if (operation.isEmpty()) {
                return operationString;
            } else {
                stack.push(operation.get());
                maxArity = Integer.max(maxArity, stack.peek().arity());
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private T parseOperands(Scanner scanner, T value) throws NumberFormatException {
        T[] operands = (T[]) new Object[maxArity]; 
        operands[0] = value;

        while (!stack.empty()) {
            Operation<T> operation = stack.pop();

            for (int i = 1; i < operation.arity(); i++) {
                if (scanner.hasNext()) {
                    operands[i] = operandParser.valueOf(scanner.next());
                } else {
                    throw new IllegalArgumentException("not enough operands");
                }
            }

            operands[0] = operation.apply(operands);
        }

        if (scanner.hasNext()) {
            throw new IllegalArgumentException("too many operands");
        }

        return operands[0];
    }


    public T compute(Scanner scanner) {
        maxArity = 1;
        stack.clear();

        String firstOperandString = parseOperations(scanner);

        if (firstOperandString == null && !stack.isEmpty()) {
            throw new IllegalArgumentException("zero operands, but more than zero operations");
        } else if (firstOperandString == null) {
            return null; // empty expression 
        }

        // throws NumberFormatException, if the first operand
        // has wrong number format
        T firstOperand = operandParser.valueOf(firstOperandString); 
        return parseOperands(scanner, firstOperand);
    }
}
