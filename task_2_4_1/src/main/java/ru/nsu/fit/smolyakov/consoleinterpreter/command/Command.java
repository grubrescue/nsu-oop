package ru.nsu.fit.smolyakov.consoleinterpreter.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * Command class for console interpreter.
 * Takes arity and list consumer as arguments, then executes list consumer with given list of arguments.
 *
 * @param <T> type of arguments
 */
@NonNull
@AllArgsConstructor
@Getter
public class Command<T> {
    private final int arity;
    private final ListConsumer<T> listConsumer;

    /**
     * Executes list consumer with given list of arguments.
     *
     * @param args list of arguments
     */
    public void execute(@NonNull List<T> args) {
        if (args.size() != arity) {
            throw new IllegalArgumentException("Wrong amount of arguments");
        }

        listConsumer.execute(args);
    }
}
