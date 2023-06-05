package ru.nsu.fit.smolyakov.consoleinterpreter.exception;

public class NoSuchCommandException extends ConsoleInterpreterException {
    public NoSuchCommandException() {
        super("No such command");
    }
}
