package ru.nsu.fit.smolyakov.caclulator.operationsprovider;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

public abstract class AbstractOperationsProvider<T> implements OperationsProvider<T> {
    private Map<String, Operation<T>> operationsMap;

    protected AbstractOperationsProvider(Map<String, Operation<T>> operationToMethod) {
        this.operationsMap = new HashMap<>(Objects.requireNonNull(operationToMethod));
    }

    protected abstract T operandValue(String name) throws NumberFormatException;

    // TODO maybe IllegalArgumentException would suit better
    @Override 
    public Operation<T> getByName(String name) throws NumberFormatException {
        var mappedOperation = operationsMap.get(name);
        if (mappedOperation == null) {
            return new Operation<>(() -> operandValue(name));
        } else {
            return mappedOperation.uncurriedCopy();
        }
    }
}
