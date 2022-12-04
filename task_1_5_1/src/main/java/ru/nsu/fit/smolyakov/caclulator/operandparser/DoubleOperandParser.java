package ru.nsu.fit.smolyakov.caclulator.operandparser;

public class DoubleOperandParser implements OperandParser<Double> { 
    public DoubleOperandParser() {}

    @Override
    public Double valueOf(String operandString) throws NumberFormatException {
        if (operandString.equals("PI")) {
            return Math.PI;
        } else if (operandString.equals("E")) {
            return Math.E;
        } else {
            return Double.valueOf(operandString);
        }
    }
}
