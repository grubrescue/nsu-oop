package ru.nsu.fit.smolyakov.caclulator.exceptions;

/**
 * Thrown to indicate that calculator appears to be in some inappropriate state.
 */
public class CalculatorException extends RuntimeException {
    /**
     * Constructs a {@code CalculatorException} with no detail message.
     */
    public CalculatorException() {
        super();
    }

    /**
     * Constructs a {@code CalculatorException} with a detail message.
     *
     * @param message message
     */
    public CalculatorException(String message) {
        super(message);
    }
}
