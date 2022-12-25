package ru.nsu.fit.smolyakov.caclulator.operation;

import ru.nsu.fit.smolyakov.caclulator.complex.Complex;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;


public class ComplexOperation extends Operation<Complex> {
    public ComplexOperation(Complex value) {
        super(value);
    }

    public ComplexOperation(Supplier<Complex> supplier) {
        super(supplier);
    }

    public ComplexOperation(UnaryOperator<Complex> unaryOperator) {
        super(unaryOperator);
    }

    public ComplexOperation(BinaryOperator<Complex> binaryOperator) {
        super(binaryOperator);
    }

    public ComplexOperation(int arity, Function<Complex> nOperator) {
        super(arity, nOperator);
    }
}
