package ru.nsu.fit.smolyakov.consoleinterpreter.exception;

public class FatalInternalCommandException extends InternalCommandException {
    public FatalInternalCommandException(String message) {
        super(message);
    }
}
