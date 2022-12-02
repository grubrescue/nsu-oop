package ru.nsu.fit.smolyakov.caclulator.exceptions;

/**
 * Thrown to indicate that calculator received an unknown lexeme on input.
 */
public class UnknownOperationException extends CalculatorException {
    /**
     * Constructs a {@code UnknownOperationException} with no detail message.
     */
    public UnknownOperationException() {
        super();
    }

    /**
     * Constructs a {@code UnknownOperationException} with a detail message.
     *
     * @param message message
     */
    public UnknownOperationException(String message) {
        super(message);
    }
}
