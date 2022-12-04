package ru.nsu.fit.smolyakov.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ru.nsu.fit.smolyakov.caclulator.Calculator;
import ru.nsu.fit.smolyakov.caclulator.operandsupplier.DoubleOperandSupplier;
import ru.nsu.fit.smolyakov.caclulator.operationsupplier.DoubleOperationSupplier;

class DoubleCalculatorTest {
    static Calculator<Double> calc;

    @BeforeAll
    static void init() {
        calc = new Calculator<Double>(new DoubleOperationSupplier(), new DoubleOperandSupplier());
    }

    @Test
    void simpleTests() {
        assertThat(calc.calculate(new Scanner("+ 5 5"))).isEqualTo(10);
        assertThat(calc.calculate(new Scanner("- 5 5"))).isEqualTo(0);
        assertThat(calc.calculate(new Scanner("* 5 5"))).isEqualTo(25);
        assertThat(calc.calculate(new Scanner("/ 5 5"))).isEqualTo(1);

        assertThat(calc.calculate(new Scanner("+ 0 0"))).isEqualTo(0);
        assertThat(calc.calculate(new Scanner("* 5 0"))).isEqualTo(0);
        assertThat(calc.calculate(new Scanner("/ 5 0"))).isEqualTo(Double.POSITIVE_INFINITY);
        assertThat(calc.calculate(new Scanner("/ -5 0"))).isEqualTo(Double.NEGATIVE_INFINITY);
    }
}
