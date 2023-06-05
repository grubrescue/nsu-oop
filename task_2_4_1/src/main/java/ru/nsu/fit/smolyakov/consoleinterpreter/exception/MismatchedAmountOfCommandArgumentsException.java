package ru.nsu.fit.smolyakov.consoleinterpreter.exception;

public class MismatchedAmountOfCommandArgumentsException extends ConsoleInterpreterException {
    public MismatchedAmountOfCommandArgumentsException() {
        super("Mismatched amount of command arguments");
    }
}
