package ru.nsu.fit.smolyakov.caclulator.operationsprovider;

import java.util.Map;

import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

public class DoubleOperationsProvider extends AbstractOperationsProvider<Double> { 
    private static Map<String, Operation<Double>> operationsMap = 
        Map.ofEntries(
            Map.entry("+", new Operation<>((a, b) -> a + b)),
            Map.entry("-", new Operation<>((a, b) -> a - b)),
            Map.entry("*", new Operation<>((a, b) -> a * b)),
            Map.entry("/", new Operation<>((a, b) -> a / b)),
            Map.entry("sin", new Operation<>((a) -> Math.sin(a))),
            Map.entry("cos", new Operation<>((a) -> Math.cos(a))),
            Map.entry("to-deg", new Operation<>((a) -> Math.toDegrees(a))),
            Map.entry("to-rad", new Operation<>((a) -> Math.toRadians(a))),
            Map.entry("PI", new Operation<>(() -> Math.PI)),
            Map.entry("E", new Operation<>(() -> Math.E)),
            Map.entry("^", new Operation<>((a, n) -> Math.pow(a, n)))
        );

    public DoubleOperationsProvider() {
        super(operationsMap);
    }

    @Override
    protected Double operandValue(String name) throws NumberFormatException {
        return Double.valueOf(name);
    }
}
