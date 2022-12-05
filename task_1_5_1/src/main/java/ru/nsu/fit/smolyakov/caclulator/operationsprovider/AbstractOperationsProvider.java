package ru.nsu.fit.smolyakov.caclulator.operationsprovider;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

public abstract class AbstractOperationsProvider<T> implements OperationsProvider<T> {
    private Map<String, Operation<T>> operationsMap;

    protected AbstractOperationsProvider(Map<String, Operation<T>> operationToMethod) {
        this.operationsMap = new HashMap<>(Objects.requireNonNull(operationToMethod));
    }

    @Override
    public Optional<Operation<T>> getByName(String name) {
        return Optional.ofNullable(operationsMap.get(name));
    }
}
