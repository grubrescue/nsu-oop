package ru.nsu.fit.smolyakov.consoleinterpreter.exception;

/**
 * Exception thrown when console interpreter has some problems.
 * These are usually caused by wrong input or wrong command,
 * and are handled "softly" by
 * {@link ru.nsu.fit.smolyakov.consoleinterpreter.interpreter.ConsoleInterpreter}.
 */
public class ConsoleInterpreterException extends RuntimeException {
    /**
     * Creates a new exception with no message.
     */
    public ConsoleInterpreterException() {
        super();
    }

    /**
     * Creates a new exception with given message.
     *
     * @param message message
     */
    public ConsoleInterpreterException(String message) {
        super(message);
    }
}
