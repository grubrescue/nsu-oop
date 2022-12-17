package ru.nsu.fit.smolyakov.caclulator;

import ru.nsu.fit.smolyakov.caclulator.operationsprovider.DoubleOperationsProvider;

/**
 * A calculator for a prefix notation with {@link Double} operands and a
 * set of operations provided by {@link DoubleOperationsProvider}. As one
 * specifies, operands are also treated as zero arguments functions.
 *
 * @see ru.nsu.fit.smolyakov.caclulator.operation.Operation
 * @see ru.nsu.fit.smolyakov.caclulator.operationsprovider.DoubleOperationsProvider
 */
public class DoubleCalculator extends Calculator<Double> {
    /**
     * Constructs an instance of {@code DoubleCalculator}.
     * Indeed, resulted class is just the same as one created this way:
     * <blockquote><pre>
     * new Calculator&lt;Double&gt;(new DoubleOperationsProvider())
     * </pre></blockquote>
     */
    public DoubleCalculator() {
        super(new DoubleOperationsProvider());
    }
}
