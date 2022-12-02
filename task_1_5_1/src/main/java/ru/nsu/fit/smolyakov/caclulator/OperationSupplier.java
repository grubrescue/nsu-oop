package ru.nsu.fit.smolyakov.caclulator;

import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

public interface OperationSupplier<T> {
    public Operation<T> operation(String name);
}
