package ru.nsu.fit.smolyakov.calculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ru.nsu.fit.smolyakov.caclulator.Calculator;
import ru.nsu.fit.smolyakov.caclulator.ComplexCalculator;
import ru.nsu.fit.smolyakov.caclulator.complex.Complex;

class ComplexCalculatorTest {
    static Calculator<Complex> calc;

    @BeforeAll
    static void init() {
        calc = new ComplexCalculator();
    }

    static Complex compute(String what) {
        return calc.compute(new Scanner(what));
    }

    @Test
    void someRepresentationTests() {
        assertThat(compute("")).isNull();

        assertThat(compute("2+3i")).isEqualTo(new Complex(2, 3));
        assertThat(compute("-2+3i")).isEqualTo(new Complex(-2, 3));
        assertThat(compute("2-3i")).isEqualTo(new Complex(2, -3));
        assertThat(compute("-2-3i")).isEqualTo(new Complex(-2, -3));
    }

    @Test
    void addTest() {
        assertThat(compute("+ 2+3i 3+2i")).isEqualTo(Complex.valueOf("5+5i"));
        assertThat(compute("+ 0+0i 0+0i")).isEqualTo(Complex.valueOf("0+0i"));
    }

    @Test
    void subTest() {
        assertThat(compute("- 2+3i 3+2i")).isEqualTo(Complex.valueOf("-1+1i"));
        assertThat(compute("- -5-5i -6-3i")).isEqualTo(Complex.valueOf("1-2i"));
    }

    @Test
    void multiplyTest() {
        assertThat(compute("* 2+3i 3+2i")).isEqualTo(Complex.valueOf("0+13i"));
        assertThat(compute("* -5-5i -6-3i")).isEqualTo(Complex.valueOf("15+45i"));
    }

    @Test
    void divideTest() {
        assertThat(compute("/ 2+3i 3+2i").r()).isCloseTo(0.92307, within(0.001));
        assertThat(compute("/ 2+3i 3+2i").i()).isCloseTo(0.38461, within(0.001));

        assertThat(compute("/ -5-5i -6-2i")).isEqualTo(new Complex(1, 0.5));
        assertThat(compute("/ 2+3i 0+0i")).isEqualTo(new Complex(Double.NaN, Double.NaN));
    }

    @Test
    void sinTest() {
        assertThat(compute("sin 2+3i").r()).isCloseTo(9.1545, within(0.001));
        assertThat(compute("sin 2+3i").i()).isCloseTo(-4.16891, within(0.001));
    }

    @Test
    void cosTest() {
        assertThat(compute("cos 2+3i").r()).isCloseTo(-4.18963, within(0.001));
        assertThat(compute("cos 2+3i").i()).isCloseTo(-9.10923, within(0.001));
    }

    @Test
    void wrongComplexFormatTest() {
        assertThatThrownBy(() -> compute("2,3i"))
            .isInstanceOf(NumberFormatException.class);
    }
}
