package ru.nsu.fit.smolyakov.caclulator.operandparser;

/**
 * 
 */
public interface OperandParser<T> {
    
    public T valueOf(String operandString) throws NumberFormatException;
}
