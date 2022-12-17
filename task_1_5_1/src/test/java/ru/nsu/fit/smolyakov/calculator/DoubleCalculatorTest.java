package ru.nsu.fit.smolyakov.calculator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.nsu.fit.smolyakov.caclulator.Calculator;
import ru.nsu.fit.smolyakov.caclulator.DoubleCalculator;

import java.util.Scanner;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class DoubleCalculatorTest {
    static Calculator<Double> calc;

    @BeforeAll
    static void init() {
        calc = new DoubleCalculator();
    }

    static Double compute(String what) {
        return calc.compute(new Scanner(what));
    }

    static Stream<Arguments> basicTestsProvider() {
        return Stream.of(
                Arguments.of("+ 5 5", 10),
                Arguments.of("- 5 5", 0),
                Arguments.of("* 5 5", 25),
                Arguments.of("/ 5 5", 1),

                Arguments.of("+ 0 0", 0),
                Arguments.of("* 5 0", 0),
                Arguments.of("/ -5 0", Double.NEGATIVE_INFINITY),
                Arguments.of("* 0 5", 0),
                Arguments.of("/ 0 5", 0),

                Arguments.of("ln E", 1),
                Arguments.of("ln ^ E 2", 2),
                Arguments.of("lg 10", 1),
                Arguments.of("lg 0.01", -2),
                Arguments.of("sqrt 16", 4),
                Arguments.of("sqrt 1", 1)
        );
    }

    static Stream<Arguments> moreDifficultTestsProvider() {
        return Stream.of(
                Arguments.of("- * / 15 - 7 + 1 1 3 + 2 + 1 1", 5),
                Arguments.of("^ 2 - 3 * 2 2", 0.5),
                Arguments.of("sin + - 1 2 1", 0),
                Arguments.of("- * ^ ^ * 2 4 2 / 1 6 / 90 4 15", 30)
        );
    }

    @Test
    void trigonometricalTests() {
        assertThat(compute("sin 0")).isEqualTo(0);
        assertThat(compute("cos 0")).isEqualTo(1);
        assertThat(compute("cos PI")).isCloseTo(-1, within(0.01));

        assertThat(compute("to-deg PI")).isCloseTo(180, within(0.01));
        assertThat(compute("to-rad 180")).isEqualTo(Math.PI);

        assertThat(compute("sin to-rad - * ^ ^ * 2 4 2 / 1 6 / 90 4 15")).isCloseTo(0.5, within(0.01));
    }

    @ParameterizedTest
    @MethodSource({"basicTestsProvider",
            "moreDifficultTestsProvider"})
    void allOtherTests(String expr, double res) {
        assertThat(compute(expr)).isEqualTo(res);
    }
}
