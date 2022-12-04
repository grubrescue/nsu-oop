package ru.nsu.fit.smolyakov.caclulator.operation;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class Operation<T> {
    private Function<T> function;
    private int arity;

    public int arity() {
        return arity;
    }

    public Operation(UnaryOperator<T> unaryOperator) {
        arity = 1;
        function = ((arr) -> unaryOperator.apply(arr[0]));
    }

    public Operation(BinaryOperator<T> binaryOperator) {
        arity = 2;
        function = ((arr) -> binaryOperator.apply(arr[0], arr[1]));
    }

    // @SafeVarargs // question
    final public T apply(T[] args) {
        if (args == null) {
            throw new IllegalArgumentException();
        } else if (args.length < arity) {
            throw new IllegalArgumentException();
        }
        return function.apply(args);
    }
}
