package ru.nsu.fit.smolyakov.caclulator.operandparser;

import ru.nsu.fit.smolyakov.caclulator.complex.Complex;

public class ComplexOperandParser implements OperandParser<Complex> { 
    public ComplexOperandParser() {}

    @Override
    public Complex valueOf(String operandString) throws NumberFormatException {
        return Complex.valueOf(operandString);
    }
}
