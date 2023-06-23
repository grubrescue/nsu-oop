package ru.nsu.fit.smolyakov.consoleinterpreter.exception;

/**
 * Exception thrown when the user tries to execute a command, but the amount of arguments is wrong.
 */
public class MismatchedAmountOfCommandArgumentsException extends ConsoleInterpreterException {
    /**
     * Creates a new exception with a "Mismatched amount of command arguments" message.
     */
    public MismatchedAmountOfCommandArgumentsException() {
        super("Mismatched amount of command arguments");
    }
}
