package ru.nsu.fit.smolyakov.caclulator.operationsupplier;

import java.util.Optional;

import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

public interface OperationSupplier<T> {
    public Optional<Operation<T>> getByName(String name);
}
