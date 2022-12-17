package ru.nsu.fit.smolyakov.caclulator;

import ru.nsu.fit.smolyakov.caclulator.complex.Complex;
import ru.nsu.fit.smolyakov.caclulator.operationsprovider.ComplexOperationsProvider;

/**
 * A calculator for a prefix notation with {@link Complex} operands and a
 * set of operations provided by {@link ComplexOperationsProvider}. As one
 * specifies, operands are also treated as zero arguments functions.
 *
 * @see ru.nsu.fit.smolyakov.caclulator.operation.Operation
 * @see ru.nsu.fit.smolyakov.caclulator.operationsprovider.ComplexOperationsProvider
 */
public class ComplexCalculator extends Calculator<Complex> {
    /**
     * Constructs an instance of {@code ComplexCalculator}.
     * Indeed, resulted class is just the same as one created this way:
     * <blockquote><pre>
     * new Calculator&lt;Complex&gt;(new ComplexOperationsProvider())}
     * </pre></blockquote>
     */
    public ComplexCalculator() {
        super(new ComplexOperationsProvider());
    }
}
