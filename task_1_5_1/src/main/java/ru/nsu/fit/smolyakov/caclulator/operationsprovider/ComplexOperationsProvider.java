package ru.nsu.fit.smolyakov.caclulator.operationsprovider;

import ru.nsu.fit.smolyakov.caclulator.complex.Complex;
import ru.nsu.fit.smolyakov.caclulator.operation.ComplexOperation;
import ru.nsu.fit.smolyakov.caclulator.operation.DoubleOperation;
import ru.nsu.fit.smolyakov.caclulator.operation.Operation;

import java.util.Map;
import java.util.Objects;

/**
 * An implementation of {@link OperationsProvider} with {@link Complex}
 * as a type of operands and minimal set of operations.
 * Namely, "+", "-", "*", "/", "sin" and "cos" operations with
 * obvious meanings are available.
 */
public class ComplexOperationsProvider extends AbstractOperationsProvider<Complex> {
    private static final DoubleOperationsProvider doubleOperationsProvider
        = new DoubleOperationsProvider();
    
    private static final Map<String, Operation<Complex>> operationsMap =
            Map.of(
                    "+", new ComplexOperation(Complex::add),
                    "-", new ComplexOperation(Complex::subtract),
                    "*", new ComplexOperation(Complex::multiply),
                    "/", new ComplexOperation(Complex::divide),
                    "sin", new ComplexOperation(Complex::sin),
                    "cos", new ComplexOperation(Complex::cos)
            );

    /**
     * Constructs an instance of {@code ComplexOperationProvider}.
     */
    public ComplexOperationsProvider() {
        super(operationsMap);
    }

    /**
     * Converts {@code operandString} into {@link Complex}.
     *
     * <p>Complex operands pattern is described in {@link Complex#valueOf(String)}.
     *
     * @param operandString a string representation of operand
     * @return a {@link Complex} number corresponding to {@code operandString}
     * @throws NumberFormatException if {@code operandString} doesn't
     *                               match operand pattern
     */
    @Override
    protected Complex parseAsOperand(String operandString) throws NumberFormatException {
        return Complex.valueOf(Objects.requireNonNull(operandString));
    }

    @Override
    public Operation<Complex> getByName(String name) throws NumberFormatException {

    }

}
