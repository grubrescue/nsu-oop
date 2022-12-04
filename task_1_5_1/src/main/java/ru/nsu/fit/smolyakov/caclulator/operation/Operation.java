package ru.nsu.fit.smolyakov.caclulator.operation;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class Operation<T> {
    private Function<T> function;
    private int arity;

    public int arity() {
        return arity;
    }

    public Operation(UnaryOperator<T> unaryOperator) {
        this(1, ((arr) -> unaryOperator.apply(arr[0])));
    }

    public Operation(BinaryOperator<T> binaryOperator) {
        this(2, ((arr) -> binaryOperator.apply(arr[0], arr[1])));
    }

    public Operation(int arity, Function<T> nOperator) {
        if (arity > 0) {
            this.arity = arity;
            this.function = Objects.requireNonNull(nOperator);
        } else {
            throw new IllegalArgumentException("arity should be more than zero");
        }
    }

    // @SafeVarargs // question
    public T apply(T[] args) {
        if (args == null) {
            throw new IllegalArgumentException();
        } else if (args.length < arity) {
            throw new IllegalArgumentException();
        }
        return function.apply(args);
    }
}
