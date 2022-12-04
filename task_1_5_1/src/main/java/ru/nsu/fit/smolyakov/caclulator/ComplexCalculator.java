package ru.nsu.fit.smolyakov.caclulator;

import ru.nsu.fit.smolyakov.caclulator.complex.Complex;
import ru.nsu.fit.smolyakov.caclulator.operandparser.ComplexOperandParser;
import ru.nsu.fit.smolyakov.caclulator.operationsupplier.ComplexOperationSupplier;

public class ComplexCalculator extends Calculator<Complex> {
    public ComplexCalculator() {
        super(new ComplexOperationSupplier(), 
              new ComplexOperandParser());
    }
}
