package ru.nsu.fit.smolyakov.caclulator;

import ru.nsu.fit.smolyakov.caclulator.operandparser.DoubleOperandParser;
import ru.nsu.fit.smolyakov.caclulator.operationsupplier.DoubleOperationSupplier;

public class DoubleCalculator extends Calculator<Double> {
    public DoubleCalculator() {
        super(new DoubleOperationSupplier(), 
              new DoubleOperandParser());
    }
}
