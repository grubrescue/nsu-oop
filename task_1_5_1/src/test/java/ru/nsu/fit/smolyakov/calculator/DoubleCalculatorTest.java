package ru.nsu.fit.smolyakov.calculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.util.Scanner;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

    static Stream<Arguments> plusMinusMultiplyDivideTestsProvider() {
        return Stream.of(
            Arguments.of("+ 5 5", 10), 
            Arguments.of("- 5 5", 0), 
            Arguments.of("* 5 5", 25), 
            Arguments.of("/ 5 5", 1), 

            Arguments.of("+ 0 0", 0), 
            Arguments.of("* 5 0", 0), 
            Arguments.of("/ -5 0", Double.NEGATIVE_INFINITY), 
            Arguments.of("* 0 5", 0), 
            Arguments.of("/ 0 5", 0)
        );
    } 

    static Stream<Arguments> moreDifficultTestsProvider() {
        return Stream.of(
            Arguments.of("- * / 15 - 7 + 1 1 3 + 2 + 1 1", 5),
            Arguments.of("sin + - 1 2 1", 0)
        );
    } 

    @Test
    void trigonometricalTests() {
        assertThat(compute("sin 0")).isEqualTo(0);
        assertThat(compute("cos 0")).isEqualTo(1);
        assertThat(compute("cos PI")).isCloseTo(-1, within(0.01));

        assertThat(compute("to-deg PI")).isCloseTo(180, within(0.01));
        assertThat(compute("to-rad 180")).isEqualTo(Math.PI);
    }

    @ParameterizedTest
    @MethodSource({"plusMinusMultiplyDivideTestsProvider",
                   "moreDifficultTestsProvider"})
    void allOtherTests(String expr, double res) {
        assertThat(compute(expr)).isEqualTo(res);
    }
}
