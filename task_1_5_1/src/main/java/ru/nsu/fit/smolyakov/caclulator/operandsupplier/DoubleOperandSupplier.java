package ru.nsu.fit.smolyakov.caclulator.operandsupplier;

public class DoubleOperandSupplier implements OperandSupplier<Double> { 
    public DoubleOperandSupplier() {}

    @Override
    public Double parse(String operandString) throws NumberFormatException {
        return Double.valueOf(operandString);
    }
}
