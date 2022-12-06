package ru.nsu.fit.smolyakov.caclulator;

import ru.nsu.fit.smolyakov.caclulator.complex.Complex;
import ru.nsu.fit.smolyakov.caclulator.operationsprovider.ComplexOperationsProvider;

public class ComplexCalculator extends Calculator<Complex> {
    public ComplexCalculator() {
        super(new ComplexOperationsProvider());
    }
}
