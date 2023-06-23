package ru.nsu.fit.smolyakov.consoleinterpreter.exception;

/**
 * Fatal exception thrown when internal command has some problems.
 * When one of these are thrown, the program probably can't continue.
 */
public class FatalInternalCommandException extends InternalCommandException {
    /**
     * Creates a new exception with a message.
     *
     * @param message message
     */
    public FatalInternalCommandException(String message) {
        super(message);
    }
}
