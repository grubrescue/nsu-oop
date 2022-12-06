package ru.nsu.fit.smolyakov.caclulator;

import ru.nsu.fit.smolyakov.caclulator.operationsprovider.DoubleOperationsProvider;

public class DoubleCalculator extends Calculator<Double> {
    public DoubleCalculator() {
        super(new DoubleOperationsProvider());
    }
}
