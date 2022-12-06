package ru.nsu.fit.smolyakov.caclulator.operationsprovider;

import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

public interface OperationsProvider<T> {
    public Operation<T> getByName(String name);
}
