package ru.nsu.fit.smolyakov.consoleinterpreter.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@NonNull
@AllArgsConstructor
@Getter
public class Command<T> {
    private final int arity;
    private final ListConsumer<T> listConsumer;

    public void execute(@NonNull List<T> args) {
        if (args.size() != arity) {
            throw new IllegalArgumentException("Wrong amount of arguments");
        }

        listConsumer.execute(args);
    }
}
