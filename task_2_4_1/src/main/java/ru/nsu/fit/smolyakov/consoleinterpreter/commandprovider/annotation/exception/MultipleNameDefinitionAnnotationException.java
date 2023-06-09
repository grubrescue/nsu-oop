package ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.exception;

import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.ConsoleCommand;

/**
 * Exception thrown when method marked with {@link ConsoleCommand} annotation has the name
 * other method already has.
 */
public class MultipleNameDefinitionAnnotationException extends UnsupportedSignatureAnnotationException {
    /**
     * Creates a new exception.
     */
    public MultipleNameDefinitionAnnotationException() {
    }
}
