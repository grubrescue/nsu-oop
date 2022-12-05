package ru.nsu.fit.smolyakov.caclulator.operationsprovider;

import java.util.Optional;

import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

public interface OperationsProvider<T> {
    public Optional<Operation<T>> getByName(String name);
}
