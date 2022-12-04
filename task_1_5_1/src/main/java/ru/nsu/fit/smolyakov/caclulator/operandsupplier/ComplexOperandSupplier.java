package ru.nsu.fit.smolyakov.caclulator.operandsupplier;

import java.util.regex.Pattern;

public class ComplexOperandSupplier implements OperandSupplier<Double> { 
    private static Pattern regexPattern = Pattern.compile("");

    public ComplexOperandSupplier() {}

    @Override
    public Double parse(String operandString) throws NumberFormatException {
        return null; // TODO
    }
}
