package ru.nsu.fit.smolyakov.caclulator.operation;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import ru.nsu.fit.smolyakov.caclulator.complex.Complex;

/**
 * Represents an operation upon operands of the same type, producing a result
 * of the same type as the operands. Implemented for internal use with
 * {@link ru.nsu.fit.smolyakov.caclulator.Calculator}.
 *
 * <p>This class imposes some restrictions preventing possible mistakes while
 * applying a function, whose amount of arguments might be not clear. Furthermore,
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
 * @param <T> a type of operands
 */
public class Operation<T> {
    private final Function<T> function;
    private final int initialArity;

    private final List<T> curriedArgs = new LinkedList<>();

    /**
     * Constructs an {@code Operation} instance as a supplier for a specified {@code value}.
     * An arity of instantiated operation will be equal to 0.
     *
     * @param value a value to be supplied by this operation
     */
    public Operation(T value) {
        this(0, ((list) -> value));
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
     * @param arity     a specified arity
     * @param operator a function to represent
     * @throws IllegalArgumentException if arity is a negative number
     */
    public Operation(int arity, Function<T> operator) {
        if (arity >= 0) {
            this.initialArity = arity;
            this.function = Objects.requireNonNull(operator);
        } else {
            throw new IllegalArgumentException("arity should be more than zero");
        }
    }

    /**
     * Turns a Double operation into Complex one, so it takes
     * Complex operands without imaginary part.
     *
     * @param op Double operation
     * @return a Complex operation
     */
    public static Operation<Complex> liftToComplex(Operation<Double> op) {
        return new Operation<>(
            op.arity(),
            (list) -> {
                var doublesList =
                    list.stream()
                        .map(Complex::toDouble)
                        .toList();

                var doubleRes =
                    op.getFunction()
                        .apply(doublesList);

                return new Complex(doubleRes, 0);
            }
        );
    }

    /**
     * Returns the arity of the current (respectively, curried) function.
     *
     * @return the arity
     */
    public int arity() {
        return initialArity - curriedArgs.size();
    }

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
     * Translates this function into one curried with specified {@code arg}.
     * Arity is decreased by 1.
     *
     * @param arg an argument of transformation
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

    /**
     * Returns a {@link Function} associated with this {@code Operation}.
     *
     * @return a function associated with this {@code Operation}
     */
    public Function<T> getFunction() {
        return function;
    }
}
