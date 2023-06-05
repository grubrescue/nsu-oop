package ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation;

/**
 * Exception thrown when method marked with {@link ConsoleCommand} annotation has wrong type parameters.
 * Actually,{@link String} is the only type that supported.
 *
 * @see CommandProviderAnnotationProcessor
 * @see ConsoleCommand
 */
public class TypeParametersUnsupportedByAnnotationException extends Exception {
    public TypeParametersUnsupportedByAnnotationException() {
        super("Only strings are supported");
    }

    public TypeParametersUnsupportedByAnnotationException(String message) {
        super(message);
    }
}
