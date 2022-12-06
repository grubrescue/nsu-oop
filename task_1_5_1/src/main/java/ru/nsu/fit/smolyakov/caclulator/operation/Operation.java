package ru.nsu.fit.smolyakov.caclulator.operation;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Represents an operation upon operands of the same type, producing a result
 * of the same type as the operands. Implemented for internal use with 
 * {@link ru.nsu.fit.smolyakov.caclulator.Calculator}.
 * 
 * <p>This class imposes some restrictions preventing possible mistakes while 
 * applying a function, whose amount of arguments might be not clear. Futhermore, 
 * default {@code apply} (or {@code compute}) method doesn't exist, as it
 * is not used by {@code Calculator}.
 * 
 * <p>Implements currying by eponymous {@link #curry(T)} method. 
 * Currying is the technique of translating the evaluation of a function that takes multiple 
 * arguments into evaluating a sequence of functions, each with a single argument. 
 * For example, currying a function {@code f} that takes three arguments creates a nested 
 * unary function g, so that {@code f(a, b, c)} is equal to {@code g(a)(b)(c)}.
 * Although this operation is destructive, a new instance of initial state of 
 * this {@code Operation} can be created by {@link #uncurriedCopy()}.
 * 
 * <p>Allows the user to get {@link #arity()} of the current (respectively, curried) function.
 * One strictly should be non-negative number, and this fact is checked by both methods and 
 * mutable methods. 
 * 
 * <p>{@link #get()} method allows to get the value supplied by 0-arity function. 
 * (So, the only way to get a value from the function represented by this class is by
 * using consistent currying).
 * 
 */
public class Operation<T> {
    private Function<T> function;
    private int initialArity;

    private List<T> curriedArgs = new LinkedList<>();

    /**
     * Returns the arity of the current (respectively, curried) function.
     * 
     * @return the arity
     */
    public int arity() {
        return initialArity - curriedArgs.size();
    }

    /**
     * Constructs an {@code Operation} instance from a specified {@code supplier}.
     * An arity of instantiated operation will be equal to 0.
     * 
     * @param supplier a function to represent
     */
    public Operation(Supplier<T> supplier) {
        this(0, ((list) -> Objects.requireNonNull(supplier.get())));
    }

    /**
     * Constructs an {@code Operation} instance from a specified {@code unaryOperator}.
     * An arity of instantiated operation will be equal to 1.
     * 
     * @param unaryOperator a function to represent
     */
    public Operation(UnaryOperator<T> unaryOperator) {
        this(1, ((list) -> Objects.requireNonNull(unaryOperator).apply(list.get(0))));
    }

    /**
     * Constructs an {@code Operation} instance from a specified {@code binaryOperator}.
     * An arity of instantiated operation will be equal to 2.
     * 
     * @param binaryOperator a function to represent
     */
    public Operation(BinaryOperator<T> binaryOperator) {
        this(2, ((list) -> Objects.requireNonNull(binaryOperator).apply(list.get(0), list.get(1))));
    }

    /**
     * Constructs an {@code Operation} instance from a specified {@code nOperator},
     * whose arity is specified by {@code arity}.
     * 
     * @param  arity  a specified arity
     * @param  nOperator  a function to represent
     * @throws IllegalArgumentException  if arity is a negative number
     */
    public Operation(int arity, Function<T> nOperator) {
        if (arity >= 0) {
            this.initialArity = arity;
            this.function = Objects.requireNonNull(nOperator);
        } else {
            throw new IllegalArgumentException("arity should be more than zero");
        }
    }

    // public T apply(List<T> args) {
    //     if (args == null) {
    //         throw new IllegalArgumentException();
    //     } 

    //     curriedArgs.addAll(args);
        
    //     if (curriedArgs.size() != initialArity) {
    //         throw new IllegalArgumentException();
    //     }

    //     return function.apply(curriedArgs);
    // }

    /**
     * Returns a value supplied by this {@code Operation}, if
     * one is a supplier. 
     * 
     * @return a value supplied by this {@code Operation}
     * @throws IllegalStateException if arity is not equal to 0
     */
    public T get() {
        if (arity() > 0) {
            throw new IllegalStateException("function require arguments!");
        }
        return function.apply(curriedArgs);
    }

    /**
     * Translated this function into one curried with specified {@code arg}.
     * Arity is decreased by 1.
     * 
     * @param  arg an argument of transformation
     * @throws IllegalStateException if arity is not a positive number
     */
    public void curry(T arg) {
        if (arity() < 1) {
            throw new IllegalStateException("function doesn't take args already");
        }

        curriedArgs.add(arg);
    }

    /**
     * Returns an uncurried copy of this {@code Operation}.
     * This instance is not changed.
     * 
     * @return an uncurried copy of this {@code Operation}
     */
    public Operation<T> uncurriedCopy() {
        return new Operation<>(initialArity, function);
    }
}
