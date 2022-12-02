package ru.nsu.fit.smolyakov.calculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ru.nsu.fit.smolyakov.caclulator.ComplexCalculator;
import ru.nsu.fit.smolyakov.caclulator.exceptions.IllegalOperationsAmountException;
import ru.nsu.fit.smolyakov.caclulator.exceptions.UnknownOperationException;

class CalculatorContractTest {
    static ComplexCalculator calc;

    @BeforeAll
    static void init() {
        calc = new ComplexCalculator();
    }

    @Test
    void notEnoughOperands() {
        assertThatThrownBy(() -> calc.compute(new Scanner("+ + 5 5")))
            .isInstanceOf(IllegalOperationsAmountException.class);
    }

    @Test
    void tooManyOperands() {
        assertThatThrownBy(() -> calc.compute(new Scanner("+ + 5 5 5 5")))
            .isInstanceOf(IllegalOperationsAmountException.class);
    }

    @Test
    void noOperands() {
        assertThatThrownBy(() -> calc.compute(new Scanner("+")))
            .isInstanceOf(IllegalOperationsAmountException.class);
    }

    @Test
    void wrongSecondOperandFormat() {
        assertThatThrownBy(() -> calc.compute(new Scanner("+ 0.54 ab0ba")))
            .isInstanceOf(UnknownOperationException.class);
    }

    @Test
    void wrongFirstOperandFormat() {
        assertThatThrownBy(() -> calc.compute(new Scanner("+ abba 6")))
            .isInstanceOf(UnknownOperationException.class);
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
