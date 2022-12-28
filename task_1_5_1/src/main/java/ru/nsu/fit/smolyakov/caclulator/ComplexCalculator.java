package ru.nsu.fit.smolyakov.caclulator;

import ru.nsu.fit.smolyakov.caclulator.complex.Complex;
import ru.nsu.fit.smolyakov.caclulator.operationsprovider.ComplexOperationsProvider;
import ru.nsu.fit.smolyakov.caclulator.operationsprovider.DoubleOperationsProvider;

/**
 * A calculator for a prefix notation with {@link Complex} operands and a
 * set of operations provided by {@link ComplexOperationsProvider}. As one
 * specifies, operands are also treated as zero arguments functions.
 *
 * <p>Also, a set of operation for {@link Double} are provided. For that,
 * {@link DoubleOperationsProvider} with its own set of operations and
 * operand pattern and {@link DoubleOperationsProvider#adapterToComplex()}
 * converter are used.
 *
 * <p>As an order of providers is important, firstly lexemes are treated as
 * if they belong to complex numbers; if latter is false, then implementation
 * assumes they are double. E.g., this situation happens, when an operation,
 * which is undefined in {@link ComplexOperationsProvider}, exists in
 * {@link DoubleOperationsProvider}.
 *
 * @see ru.nsu.fit.smolyakov.caclulator.operation.Operation
 * @see ru.nsu.fit.smolyakov.caclulator.operationsprovider.ComplexOperationsProvider
 */
public class ComplexCalculator extends Calculator<Complex> {
    /**
     * Constructs an instance of {@code ComplexCalculator}.
     * Indeed, resulted class is just the same as one created this way:
     * <blockquote><pre>
     * new Calculator&lt;Complex&gt;(new ComplexOperationsProvider())
     * </pre></blockquote>
     */
    public ComplexCalculator() {
        super(new DoubleOperationsProvider().adapterToComplex());
        insertProvider(new ComplexOperationsProvider());
    }
}
