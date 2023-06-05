package ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation;

public class TypeParametersUnsupportedByAnnotationException extends Exception {
    public TypeParametersUnsupportedByAnnotationException() {
        super("Only strings are supported");
    }

    public TypeParametersUnsupportedByAnnotationException(String message) {
        super(message);
    }
}
