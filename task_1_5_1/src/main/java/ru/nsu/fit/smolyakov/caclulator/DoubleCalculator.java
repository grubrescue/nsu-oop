package ru.nsu.fit.smolyakov.caclulator;

import ru.nsu.fit.smolyakov.caclulator.operandsupplier.DoubleOperandSupplier;
import ru.nsu.fit.smolyakov.caclulator.operationsupplier.DoubleOperationSupplier;

public class DoubleCalculator extends Calculator<Double> {
    public DoubleCalculator() {
        super(new DoubleOperationSupplier(), 
              new DoubleOperandSupplier());
    }
}
