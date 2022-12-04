package ru.nsu.fit.smolyakov.caclulator;

import java.util.Optional;
import java.util.Scanner;
import java.util.Stack;

import ru.nsu.fit.smolyakov.caclulator.operandsupplier.OperandSupplier;
import ru.nsu.fit.smolyakov.caclulator.operation.Operation;
import ru.nsu.fit.smolyakov.caclulator.operationsupplier.OperationSupplier;

public class Calculator<T> {
    private OperationSupplier<T> operationSupplier;
    private OperandSupplier<T> operandSupplier;

    private Stack<Operation<T>> stack = new Stack<>();

    public Calculator(OperationSupplier<T> operationSupplier, 
                      OperandSupplier<T> operandSupplier) {
        this.operationSupplier = operationSupplier;
        this.operandSupplier = operandSupplier;
    }

    private String parseOperations(Scanner scanner) {
        while (scanner.hasNext()) {
            String op = scanner.next();
            Optional<Operation<T>> parsedOperation = operationSupplier.operation(op);

            if (parsedOperation.isEmpty()) {
                return op;
            } else {
                stack.push(parsedOperation.get());
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private T parseOperands(Scanner scanner, T value) throws NumberFormatException {
        T[] operands = (T[]) new Object[666]; // TODO remove magic numbers
                                              // now 666 is max arity

        operands[0] = value;

        while (!stack.empty()) {
            Operation<T> operation = stack.pop();

            for (int i = 1; i < operation.arity(); i++) {
                if (scanner.hasNext()) {
                    operands[i] = operandSupplier.parse(scanner.next());
                } else {
                    throw new IllegalArgumentException("amount of operands is incorrect"); // TODO change
                }
            }

            operands[0] = operation.apply(operands);
        }

        if (scanner.hasNext()) {
            throw new IllegalArgumentException("amount of operands is incorrect"); // TODO change
        }

        return operands[0];
    }

    public T calculate(Scanner scanner) {
        stack.clear();

        T firstOperand = operandSupplier.parse(parseOperations(scanner));
        if (firstOperand == null) {
            throw new IllegalArgumentException(); //TODO change type
        }

        return parseOperands(scanner, firstOperand);
    }
}
