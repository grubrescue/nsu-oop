package ru.nsu.fit.smolyakov.consoleinterpreter.command;

import lombok.NonNull;

import java.util.function.Consumer;

public class SingleArgCommand<T> extends Command<T> {
    public SingleArgCommand(@NonNull Consumer<T> consumer) {
        super(0, singleArgList -> consumer.accept(singleArgList.get(0)));
    }
}
