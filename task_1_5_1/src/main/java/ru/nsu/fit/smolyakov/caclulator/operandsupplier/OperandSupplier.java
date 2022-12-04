package ru.nsu.fit.smolyakov.caclulator.operandsupplier;

public interface OperandSupplier<T> {
    public T parse(String operandString) throws NumberFormatException;
}
