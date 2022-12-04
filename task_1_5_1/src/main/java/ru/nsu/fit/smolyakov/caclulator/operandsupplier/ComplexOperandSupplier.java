package ru.nsu.fit.smolyakov.caclulator.operandsupplier;

import java.util.regex.Pattern;

import ru.nsu.fit.smolyakov.caclulator.complex.Complex;

public class ComplexOperandSupplier implements OperandSupplier<Complex> { 
    private static Pattern regexPattern = Pattern.compile("");

    public ComplexOperandSupplier() {}

    @Override
    public Complex parse(String operandString) throws NumberFormatException {
        return null; // TODO
    }
}
