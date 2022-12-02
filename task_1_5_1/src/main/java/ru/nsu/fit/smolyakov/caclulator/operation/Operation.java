package ru.nsu.fit.smolyakov.caclulator.operation;

public class Operation<T> {
    public int arity();

    public T apply(T[] args);
}
