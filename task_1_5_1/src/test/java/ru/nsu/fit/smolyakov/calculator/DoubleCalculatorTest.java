package ru.nsu.fit.smolyakov.calculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ru.nsu.fit.smolyakov.caclulator.Calculator;
import ru.nsu.fit.smolyakov.caclulator.DoubleCalculator;

class DoubleCalculatorTest {
    static Calculator<Double> calc;

    @BeforeAll
    static void init() {
        calc = new DoubleCalculator();
    }

    static Double compute(String what) {
        return calc.compute(new Scanner(what));
    }

    @Test
    void simplePlusMinusTests() {
        assertThat(compute("+ 5 5")).isEqualTo(10);
        assertThat(compute("- 5 5")).isEqualTo(0);
        assertThat(compute("* 5 5")).isEqualTo(25);
        assertThat(compute("/ 5 5")).isEqualTo(1);

        assertThat(compute("+ 0 0")).isEqualTo(0);
        assertThat(compute("* 5 0")).isEqualTo(0);
        assertThat(compute("/ 5 0")).isEqualTo(Double.POSITIVE_INFINITY);
        assertThat(compute("/ -5 0")).isEqualTo(Double.NEGATIVE_INFINITY);
        assertThat(compute("* 0 5")).isEqualTo(0);
        assertThat(compute("/ 0 5")).isEqualTo(0);
    }

    @Test
    void simpleTrigonometricalTests() {
        assertThat(compute("sin 0")).isEqualTo(0);
        assertThat(compute("cos 0")).isEqualTo(1);
        assertThat(compute("cos PI")).isCloseTo(-1, within(0.01));

        assertThat(compute("to-deg PI")).isCloseTo(180, within(0.01));
        assertThat(compute("to-rad 180")).isEqualTo(Math.PI);
    }

    @Test
    void moreDifficultTests() {
        assertThat(compute("sin + - 1 2 1")).isEqualTo(0);
    }
}
