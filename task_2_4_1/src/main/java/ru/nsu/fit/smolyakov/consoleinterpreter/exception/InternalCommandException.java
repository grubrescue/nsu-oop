package ru.nsu.fit.smolyakov.consoleinterpreter.exception;

/**
 * Exception thrown when internal command has some problems.
 */
public class InternalCommandException extends ConsoleInterpreterException {
    /**
     * Creates a new exception with a message.
     *
     * @param message message
     */
    public InternalCommandException(String message) {
        super(message);
    }
}
