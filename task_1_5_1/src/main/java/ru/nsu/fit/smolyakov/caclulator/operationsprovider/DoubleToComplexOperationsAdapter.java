package ru.nsu.fit.smolyakov.caclulator.operationsprovider;

import ru.nsu.fit.smolyakov.caclulator.complex.Complex;
import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

import java.util.Objects;

class DoubleToComplexOperationsAdapter implements OperationsProvider<Complex> {
    private final DoubleOperationsProvider provider;

    DoubleToComplexOperationsAdapter(DoubleOperationsProvider provider) {
        this.provider = Objects.requireNonNull(provider);
    }

    /**
     * Returns an {@link Operation} associated with specified {@code name}.
     * If there is no such operators, then {@code name} is parsed
     * according to operand pattern.
     *
     * @param name a name of an operation
     * @return an operation associated with {@code name}
     * @throws NumberFormatException if {@code} name is either
     *                               not connected to any operation and doesn't match
     *                               the format of operand
     */
    @Override
    public Operation<Complex> getByName(String name) {
        return Operation.liftToComplex(provider.getByName(name));
    }
}
