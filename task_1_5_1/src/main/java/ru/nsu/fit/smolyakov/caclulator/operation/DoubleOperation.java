package ru.nsu.fit.smolyakov.caclulator.operation;

import ru.nsu.fit.smolyakov.caclulator.complex.Complex;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class DoubleOperation extends Operation<Double> {
    public DoubleOperation(Double value) {
        super(value);
    }

    public DoubleOperation(Supplier<Double> supplier) {
        super(supplier);
    }

    public DoubleOperation(UnaryOperator<Double> unaryOperator) {
        super(unaryOperator);
    }

    public DoubleOperation(BinaryOperator<Double> binaryOperator) {
        super(binaryOperator);
    }

    public DoubleOperation(int arity, Function<Double> nOperator) {
        super(arity, nOperator);
    }

    public ComplexOperation liftToComplex() {
        return new ComplexOperation(
            arity(),
                (list) -> {
                    var doublesList =
                        list.stream()
                            .map(Complex::toDouble)
                            .toList();

                    var doubleRes =
                        this.getFunction()
                            .apply(doublesList);

                    return new Complex(doubleRes, 0);
                }
            );
    }
}
