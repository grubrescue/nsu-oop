package ru.nsu.fit.smolyakov.consoleinterpreter.command;

import lombok.NonNull;

public class NoArgsCommand<T> extends Command<T> {
    public NoArgsCommand(@NonNull Runnable runnable) {
        super(0, ignored -> runnable.run());
    }
}
