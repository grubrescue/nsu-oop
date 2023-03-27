package ru.nsu.fit.smolyakov.pizzeria.customer;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaCustomerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Creates instances of {@link FrequentCustomer} by
 * {@link #instance(OrderDescription, PizzeriaCustomerService, int)} method.
 *
 * <p>Used implementation ({@link FrequentCustomerImpl} requires an
 * {@link java.util.concurrent.ScheduledExecutorService} to schedule tasks;
 * one factory shares one thread pool between all created customers in terms
 * of improving performance.
 */
public class FrequentCustomerFactory {
    private final ScheduledExecutorService threadPool =
        Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    public FrequentCustomer instance(OrderDescription orderDescription,
                                     PizzeriaCustomerService pizzeriaCustomerService,
                                     int frequencyMillis) {
        return new FrequentCustomerImpl(threadPool, orderDescription, pizzeriaCustomerService, frequencyMillis);
    }
}
