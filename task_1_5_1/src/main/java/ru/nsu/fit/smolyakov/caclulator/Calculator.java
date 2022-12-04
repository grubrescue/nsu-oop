package ru.nsu.fit.smolyakov.caclulator;

import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.Stack;

import ru.nsu.fit.smolyakov.caclulator.operandparser.OperandParser;
import ru.nsu.fit.smolyakov.caclulator.operation.Operation;
import ru.nsu.fit.smolyakov.caclulator.operationsupplier.OperationSupplier;

public class Calculator<T> {
    private OperationSupplier<T> operationSupplier;
    private OperandParser<T> operandParser;

    private Stack<Operation<T>> stack = new Stack<>();

    /**
     * 
     * @param operationSupplier
     * @param operandParser
     * 
     */
    public Calculator(OperationSupplier<T> operationSupplier, 
                      OperandParser<T> operandParser) {
        this.operationSupplier = Objects.requireNonNull(operationSupplier);
        this.operandParser = Objects.requireNonNull(operandParser);
    }


    private String parseOperations(Scanner scanner) {
        while (scanner.hasNext()) {
            String operationString = scanner.next();
            Optional<Operation<T>> operation = operationSupplier.getByName(operationString);

            if (operation.isEmpty()) {
                return operationString;
            } else {
                stack.push(operation.get());
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private T parseOperands(Scanner scanner, T value) throws NumberFormatException {
        T[] operands = (T[]) new Object[666]; // TODO remove magic numbers
                                              // for now 666 is max arity

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
