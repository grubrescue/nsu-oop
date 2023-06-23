package ru.nsu.fit.smolyakov.consoleinterpreter.command;

import lombok.NonNull;

/**
 * Command class for console interpreter.
 * A wrapper class for {@link Command} with arity 0 and a {@link Runnable} converted to list consumer.
 *
 * @param <T> type of arguments
 */
public class NoArgsCommand<T> extends Command<T> {
    /**
     * Creates a new command with arity 0 and {@link Runnable} converted to list consumer.
     *
     * @param runnable a runnable to be converted to list consumer
     */
    public NoArgsCommand(@NonNull Runnable runnable) {
        super(0, ignored -> runnable.run());
    }
}
