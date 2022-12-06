package ru.nsu.fit.smolyakov.caclulator.operationsprovider;

import java.util.Map;

import ru.nsu.fit.smolyakov.caclulator.complex.Complex;
import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

public class ComplexOperationsProvider extends AbstractOperationsProvider<Complex> { 
    private static Map<String, Operation<Complex>> operationsMap = 
        Map.of(
            "+", new Operation<>((a, b) -> a.add(b)),
            "-", new Operation<>((a, b) -> a.substract(b)),
            "*", new Operation<>((a, b) -> a.multiply(b)),
            "/", new Operation<>((a, b) -> a.divide(b)),
            "sin", new Operation<>((a) -> a.sin()),
            "cos", new Operation<>((a) -> a.cos())
        );

    public ComplexOperationsProvider() {
        super(operationsMap);
    }

    @Override
    protected Complex operandValue(String name) throws NumberFormatException {
        return Complex.valueOf(name);
    }
}
