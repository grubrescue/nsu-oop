package ru.nsu.fit.smolyakov.pizzeria;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class PizzeriaTest {
    @Test
    @Timeout(value = 31)
    void justMainTest() {
        Application.main(null);
    }
}
