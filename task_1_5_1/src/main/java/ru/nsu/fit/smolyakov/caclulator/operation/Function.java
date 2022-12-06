package ru.nsu.fit.smolyakov.caclulator.operation;

import java.util.List;

public interface Function<T> {
    public T apply(List<T> args);
}
