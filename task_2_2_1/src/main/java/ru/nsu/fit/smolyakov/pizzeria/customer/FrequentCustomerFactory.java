package ru.nsu.fit.smolyakov.pizzeria.customer;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOrderService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class FrequentCustomerFactory {
    private final ScheduledExecutorService threadPool =
        Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    public FrequentCustomer instance(OrderDescription orderDescription,
                                     PizzeriaOrderService pizzeriaOrderService,
                                     int frequencyMillis) {
        return new FrequentCustomerImpl(threadPool, orderDescription, pizzeriaOrderService, frequencyMillis);
    }
}
