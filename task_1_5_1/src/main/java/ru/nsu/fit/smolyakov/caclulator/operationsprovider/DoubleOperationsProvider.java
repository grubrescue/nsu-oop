package ru.nsu.fit.smolyakov.caclulator.operationsprovider;

import ru.nsu.fit.smolyakov.caclulator.operation.DoubleOperation;
import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

import java.util.Map;
import java.util.Objects;

/**
 * An implementation of {@link OperationsProvider} with {@link Double} as a
 * type of operand and a set of basic arithmetic operations, such as
 * "+", "-", "*", "/", "^" with obvious meanings, some trigonometrical
 * operations - "sin" and "cos" - and "to-deg" and "to-rad", providing conversion
 * from radians to degree and backwards.
 *
 * <p>This provider also contains {@link Math#PI} and {@link Math#E} constants,
 * also represented as suppliers.
 */
public class DoubleOperationsProvider extends AbstractOperationsProvider<Double> {
    private static final Map<String, Operation<Double>> operationsMap =
            Map.ofEntries(
                    Map.entry("+", new DoubleOperation((a, b) -> a + b)),
                    Map.entry("-", new DoubleOperation((a, b) -> a - b)),
                    Map.entry("*", new DoubleOperation((a, b) -> a * b)),
                    Map.entry("/", new DoubleOperation((a, b) -> a / b)),
                    Map.entry("sin", new DoubleOperation(Math::sin)),
                    Map.entry("cos", new DoubleOperation(Math::cos)),
                    Map.entry("to-deg", new DoubleOperation(Math::toDegrees)),
                    Map.entry("to-rad", new DoubleOperation(Math::toRadians)),
                    Map.entry("PI", new DoubleOperation(() -> Math.PI)),
                    Map.entry("E", new DoubleOperation(() -> Math.E)),
                    Map.entry("^", new DoubleOperation(Math::pow)),
                    Map.entry("ln", new DoubleOperation(Math::log)),
                    Map.entry("lg", new DoubleOperation(Math::log10)),
                    Map.entry("sqrt", new DoubleOperation(Math::sqrt))
            );

    /**
     * Constructs an instance of {@code ComplexOperationProvider}.
     */
    public DoubleOperationsProvider() {
        super(operationsMap);
    }

    /**
     * Converts {@code operandString} into {@link Double}.
     *
     * <p>Complex operands pattern is described in {@link Double#valueOf(String)}.
     *
     * @param operandString a string representation of operand
     * @return a {@link Double} number corresponding to {@code operandString}
     * @throws NumberFormatException if {@code operandString} doesn't
     *                               match operand pattern
     */
    @Override
    protected Double parseAsOperand(String operandString) throws NumberFormatException {
        return Double.valueOf(Objects.requireNonNull(operandString));
    }
}
