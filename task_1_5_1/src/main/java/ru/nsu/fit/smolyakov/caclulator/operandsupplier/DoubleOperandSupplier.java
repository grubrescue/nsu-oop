package ru.nsu.fit.smolyakov.caclulator.operandsupplier;

public class DoubleOperandSupplier implements OperandSupplier<Double> { 
    public DoubleOperandSupplier() {}

    @Override
    public Double parse(String operandString) throws NumberFormatException {
        if (operandString.equals("PI")) {
            return Math.PI;
        } else if (operandString.equals("E")) {
            return Math.E;
        } else {
            return Double.valueOf(operandString);
        }
    }
}
