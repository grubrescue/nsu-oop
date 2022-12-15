package ru.nsu.fit.smolyakov.caclulator.operationsprovider;

import ru.nsu.fit.smolyakov.caclulator.complex.Complex;
import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

import java.util.Map;
import java.util.Objects;

public class DoubleOperationsProvider extends AbstractOperationsProvider<Double> {
    private static final Map<String, Operation<Double>> operationsMap =
            Map.ofEntries(
                    Map.entry("+", new Operation<>((a, b) -> a + b)),
                    Map.entry("-", new Operation<>((a, b) -> a - b)),
                    Map.entry("*", new Operation<>((a, b) -> a * b)),
                    Map.entry("/", new Operation<>((a, b) -> a / b)),
                    Map.entry("sin", new Operation<>(Math::sin)),
                    Map.entry("cos", new Operation<>(Math::cos)),
                    Map.entry("to-deg", new Operation<>(Math::toDegrees)),
                    Map.entry("to-rad", new Operation<>(Math::toRadians)),
                    Map.entry("PI", new Operation<>(() -> Math.PI)),
                    Map.entry("E", new Operation<>(() -> Math.E)),
                    Map.entry("^", new Operation<>(Math::pow))
            );

    /**
     * Constructs an instance of {@code ComplexOperationProvider} with a set
     * of basic arithmetic operations, such as "+", "-", "*", "/", "^" with
     * obvious meanings, some trigonometrical operations - "sin" and "cos" -
     * and "to-deg" and "to-rad", providing conversion from radians to degree and
     * backwards.
     *
     * <p>This provider also contains "PI" and "E" constants.
     */
    public DoubleOperationsProvider() {
        super(operationsMap);
    }

    /**
     * Converts {@code operandString} into {@link Double}.
     *
     * <p>Complex operands pattern is described in {@link Double#valueOf(String)}.
     *
     * @param  operandString a string representation of operand
     * @return a {@link Double} number corresponding to {@code operandString}
     * @throws NumberFormatException if {@code operandString} doesn't
     *                               match operand pattern
     */
    @Override
    protected Double operandValue(String operandString) throws NumberFormatException {
        return Double.valueOf(Objects.requireNonNull(operandString));
    }
}
