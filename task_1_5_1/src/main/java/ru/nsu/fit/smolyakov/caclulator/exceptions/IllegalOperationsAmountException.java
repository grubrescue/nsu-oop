package ru.nsu.fit.smolyakov.caclulator.exceptions;

/**
 * Thrown to indicate that calculator received incorrect amount of lexemes on input.
 */
public class IllegalOperationsAmountException extends CalculatorException {
    /**
     * Constructs a {@code IllegalOperationsAmountException} with no detail message.
     */
    public IllegalOperationsAmountException() {
        super();
    }

    /**
     * Constructs a {@code IllegalOperationsAmountException} with a detail message.
     *
     * @param message message
     */
    public IllegalOperationsAmountException(String message) {
        super(message);
    }
}
