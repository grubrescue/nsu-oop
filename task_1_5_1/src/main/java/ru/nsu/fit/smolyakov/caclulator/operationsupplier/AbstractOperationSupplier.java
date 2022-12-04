package ru.nsu.fit.smolyakov.caclulator.operationsupplier;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

public abstract class AbstractOperationSupplier<T> implements OperationSupplier<T> {
    private Map<String, Operation<T>> operationsMap;

    protected AbstractOperationSupplier(Map<String, Operation<T>> operationToMethod) {
        this.operationsMap = new HashMap<>(Objects.requireNonNull(operationToMethod));
    }

    @Override
    public Optional<Operation<T>> operation(String name) {
        return Optional.ofNullable(operationsMap.get(name));
    }
}
