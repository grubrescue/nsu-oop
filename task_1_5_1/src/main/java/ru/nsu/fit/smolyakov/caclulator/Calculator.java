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

    private Stack<Operation<T>> stack;

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
    private T parseOperand(Scanner scanner, T value) {
        while (!stack.empty()) {
            Operation<T> operation = stack.pop();
            int arity = operation.arity();

            T[] operands = (T[]) new Object[arity];

            for (int i = 0; i < arity; i++) {
                if (scanner.hasNext()) {
                    operands[i] = operandSupplier.parse(scanner.next());
                } else {
                    throw new IllegalArgumentException("amount of operands is incorrect"); // TODO change
                }
            }

            value = operation.apply(operands);
        }

        if (scanner.hasNext()) {
            throw new IllegalArgumentException("amount of operands is incorrect"); // TODO change
        }

        return value;
    }

    public T calculate(Scanner scanner) {
        T firstOperand = operandSupplier.parse(parseOperations(scanner));
        if (firstOperand == null) {
            throw new IllegalArgumentException(); //TODO change type
        }

        return firstOperand;
    }
}
