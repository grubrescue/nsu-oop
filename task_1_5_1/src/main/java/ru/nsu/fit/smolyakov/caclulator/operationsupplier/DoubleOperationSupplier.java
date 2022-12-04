package ru.nsu.fit.smolyakov.caclulator.operationsupplier;

import java.util.Map;

import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

public class DoubleOperationSupplier extends AbstractOperationSupplier<Double> { 
    private static Map<String, Operation<Double>> operationsMap = 
        Map.of(
            "+", new Operation<>((a, b) -> a + b),
            "-", new Operation<>((a, b) -> a - b),
            "*", new Operation<>((a, b) -> a * b),
            "/", new Operation<>((a, b) -> a / b),
            "sin", new Operation<>((a) -> Math.sin(a)),
            "cos", new Operation<>((a) -> Math.cos(a)),
            "to-deg", new Operation<>((a) -> Math.toDegrees(a)),
            "to-rad", new Operation<>((a) -> Math.toRadians(a))
        );

    public DoubleOperationSupplier() {
        super(operationsMap);
    }
}
