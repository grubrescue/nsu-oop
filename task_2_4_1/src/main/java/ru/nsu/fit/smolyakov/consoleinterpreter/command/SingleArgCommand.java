package ru.nsu.fit.smolyakov.consoleinterpreter.command;

import lombok.NonNull;

import java.util.function.Consumer;

/**
 * Command class for console interpreter.
 * A wrapper class for {@link Command} with arity 1 and a {@link Consumer} converted to list consumer.
 *
 * @param <T> type of arguments
 */
public class SingleArgCommand<T> extends Command<T> {
    /**
     * Creates a new command with arity 1 and a {@link Consumer} converted to list consumer.
     *
     * @param consumer a consumer to be converted to list consumer
     */
    public SingleArgCommand(@NonNull Consumer<T> consumer) {
        super(1, singleArgList -> consumer.accept(singleArgList.get(0)));
    }
}
