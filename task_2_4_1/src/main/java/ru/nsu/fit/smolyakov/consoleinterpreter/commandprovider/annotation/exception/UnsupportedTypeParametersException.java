package ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.exception;

import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.ConsoleCommand;

/**
 * Exception thrown when method marked with {@link ConsoleCommand} annotation has wrong type parameters
 * (non-{@link String}s, for default).
 */
public class UnsupportedTypeParametersException extends UnsupportedSignatureAnnotationException {
    /**
     * Creates a new exception with an "Only strings are supported" message.
     */
    public UnsupportedTypeParametersException() {
        super("Only strings are supported");
    }
}
