package ru.nsu.fit.smolyakov.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;

import ru.nsu.fit.smolyakov.caclulator.DoubleOperationSupplier;

class CalculatorTest {
    @Test
    void a() throws NoSuchFieldException, SecurityException { 
        assertThat(new String()).isNotNull();

        var a = new DoubleOperationSupplier(Map.of("a", "b"));
        a.operation("ab");
        
    }
}
