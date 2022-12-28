package ru.nsu.fit.smolyakov.calculator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.caclulator.Calculator;
import ru.nsu.fit.smolyakov.caclulator.ComplexCalculator;

import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalculatorContractTest {
    static ComplexCalculator calc;

    @BeforeAll
    static void init() {
        calc = new ComplexCalculator();
    }

    @Test
    void notEnoughOperands() {
        assertThatThrownBy(() -> calc.compute(new Scanner("+ + 5 5")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void tooManyOperands() {
        assertThatThrownBy(() -> calc.compute(new Scanner("+ + 5 5 5 5")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void noOperands() {
        assertThatThrownBy(() -> calc.compute(new Scanner("+")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void wrongSecondOperandFormat() {
        assertThatThrownBy(() -> calc.compute(new Scanner("+ 0.54 ab0ba")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void wrongFirstOperandFormat() {
        assertThatThrownBy(() -> calc.compute(new Scanner("+ abba 6")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void emptyExpression() {
        assertThat(calc.compute(new Scanner(""))).isNull();
    }

    @Test
    void justNumberExpression() {
        assertThat(calc.compute(new Scanner("E")).r()).isEqualTo(Math.E);
    }
}
