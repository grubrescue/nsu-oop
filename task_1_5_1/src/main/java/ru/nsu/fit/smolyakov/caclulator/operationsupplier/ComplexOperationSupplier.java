package ru.nsu.fit.smolyakov.caclulator.operationsupplier;

import java.util.Map;

import ru.nsu.fit.smolyakov.caclulator.complex.Complex;
import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

public class ComplexOperationSupplier extends AbstractOperationSupplier<Complex> { 
    private static Map<String, Operation<Complex>> operationsMap = 
        Map.of(
            "+", new Operation<>((a, b) -> a.add(b)),
            "-", new Operation<>((a, b) -> a.substract(b)),
            "*", new Operation<>((a, b) -> a.multiply(b)),
            "/", new Operation<>((a, b) -> a.divide(b)),
            "sin", new Operation<>((a) -> a.sin()),
            "cos", new Operation<>((a) -> a.cos())
        );

    public ComplexOperationSupplier() {
        super(operationsMap);
    }
}
