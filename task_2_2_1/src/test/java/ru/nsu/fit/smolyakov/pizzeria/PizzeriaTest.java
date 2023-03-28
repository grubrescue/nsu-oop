package ru.nsu.fit.smolyakov.pizzeria;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import ru.nsu.fit.smolyakov.pizzeria.customer.FrequentCustomerFactory;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaCustomerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaImpl;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOwnerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.Address;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;

public class PizzeriaTest {
    @Test
    @Timeout(value = 11)
    void justTest() {
        Application.main(null);
    }
}
