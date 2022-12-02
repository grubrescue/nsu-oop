package ru.nsu.fit.smolyakov.caclulator;

import java.util.HashMap;
import java.util.Map;

import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

public abstract class AbstractOperationSupplier<T> /*implements OperationSupplier<T>*/ {
    // map "name of op in text" -> "method name"
    private Map<String, String> operationToMethod;

    protected AbstractOperationSupplier(Map<String, String> operationToMethod) {
        this.operationToMethod = new HashMap<>(operationToMethod);
    }

    // @Override
    public Operation<T> operation(String name) throws NoSuchFieldException, SecurityException {
        Operation<T> operation;
        try {
            this.getClass().getField("name");
        }
        System.out.println(this.getClass().getField("name"));
        return null;
    }

    // TODO reflect inherited fields
}
