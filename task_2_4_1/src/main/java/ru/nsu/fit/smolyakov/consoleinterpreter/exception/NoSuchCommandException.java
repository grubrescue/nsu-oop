package ru.nsu.fit.smolyakov.consoleinterpreter.exception;

/**
 * Exception thrown when the user tries to call a command that doesn't exist.
 */
public class NoSuchCommandException extends ConsoleInterpreterException {
    /**
     * Creates a new exception with a "No such command" message.
     */
    public NoSuchCommandException() {
        super("No such command");
    }
}
