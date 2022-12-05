package ru.nsu.fit.smolyakov.calculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ru.nsu.fit.smolyakov.caclulator.complex.Complex;

class ComplexTest {
    static Stream<Arguments> complexStringsProvider() {
        return Stream.of(
            Arguments.of("2+3i", new Complex(2, 3)),
            Arguments.of("-2+3i", new Complex(-2, 3)),
            Arguments.of("2-3i", new Complex(2, -3)),
            Arguments.of("-2-3i", new Complex(-2, -3)),

            Arguments.of("2-3.7i", new Complex(2, -3.7)),
            Arguments.of("-2.5-3.6i", new Complex(-2.5, -3.6))
        );
    } 

    static Stream<String> wrongFormatComplexStringsProvider() {
        return Stream.of(
            "",
            "2,3i",
            "2+3",
            "2+3j",
            "2.+3i",
            "--2.0+3i",
            "2e5+3i",       
            "2,5+3e4i"  
        );
    }

    @ParameterizedTest
    @MethodSource("complexStringsProvider")
    void regularFormatValueOfTests(String complexString, Complex complex) {
        assertThat(Complex.valueOf(complexString)).isEqualTo(complex);
    }

    @ParameterizedTest
    @MethodSource("wrongFormatComplexStringsProvider")
    void wrongFormatValueOfTest(String complexString) {
        assertThatThrownBy(() -> Complex.valueOf(complexString))
            .isInstanceOf(NumberFormatException.class);
    }

    @ParameterizedTest
    @MethodSource("complexStringsProvider")
    void toStringTest(String complexString, Complex complex) {
        assertThat(Complex.valueOf(complexString).toString()).isEqualTo(complexString);
    }
}
