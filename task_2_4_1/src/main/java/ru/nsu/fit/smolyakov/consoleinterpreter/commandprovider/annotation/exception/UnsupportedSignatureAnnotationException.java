package ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.exception;

import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.CommandProviderAnnotationProcessor;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.ConsoleCommand;

/**
 * Exception thrown when method marked with {@link ConsoleCommand} annotation has wrong type parameters.
 * Actually,{@link String} is the only type that supported.
 *
 * @see CommandProviderAnnotationProcessor
 * @see ConsoleCommand
 */
public class UnsupportedSignatureAnnotationException extends RuntimeException {
    /**
     * Creates a new exception.
     */
    public UnsupportedSignatureAnnotationException() {
        super();
    }

    /**
     * Creates a new exception with a message.
     * @param message message
     */
    public UnsupportedSignatureAnnotationException(String message) {
        super(message);
    }
}
