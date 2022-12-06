package ru.nsu.fit.smolyakov.caclulator.operation;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Operation<T> {
    private Function<T> function;
    private int initialArity;

    private List<T> curriedArgs = new LinkedList<>();

    public int arity() {
        return initialArity - curriedArgs.size();
    }

    public Operation(Supplier<T> supplier) {
        this(0, ((list) -> Objects.requireNonNull(supplier.get())));
    }

    public Operation(UnaryOperator<T> unaryOperator) {
        this(1, ((list) -> Objects.requireNonNull(unaryOperator).apply(list.get(0))));
    }

    public Operation(BinaryOperator<T> binaryOperator) {
        this(2, ((list) -> Objects.requireNonNull(binaryOperator).apply(list.get(0), list.get(1))));
    }

    public Operation(int initialArity, Function<T> nOperator) {
        if (initialArity >= 0) {
            this.initialArity = initialArity;
            this.function = Objects.requireNonNull(nOperator);
        } else {
            throw new IllegalArgumentException("arity should be more than zero");
        }
    }

    // @SafeVarargs // question
    public T apply(List<T> args) {
        if (args == null) {
            throw new IllegalStateException();
        } 

        curriedArgs.addAll(args);
        
        if (curriedArgs.size() != initialArity) {
            throw new IllegalStateException();
        }

        return function.apply(curriedArgs);
    }

    /**
     * 
     * @return
     */
    public T apply() {
        if (arity() > 0) {
            throw new IllegalStateException("function require arguments!");
        }
        return function.apply(curriedArgs);
    }

    public void curry(T arg) {
        if (arity() < 1) {
            throw new IllegalStateException("function doesn't take args already");
        }

        curriedArgs.add(arg);
    }
}
