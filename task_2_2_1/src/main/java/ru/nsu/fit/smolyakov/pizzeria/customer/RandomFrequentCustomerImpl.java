package ru.nsu.fit.smolyakov.pizzeria.customer;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaCustomerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;

import java.security.SecureRandom;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * An implementation of {@link RandomFrequentCustomer} interface.
 * May be instantiated by
 * {@link RandomFrequentCustomerFactory#instance(
 *OrderDescription, PizzeriaCustomerService, int)}.
 */
public class RandomFrequentCustomerImpl implements RandomFrequentCustomer {
    private final OrderDescription orderDescription;
    private final PizzeriaCustomerService pizzeriaCustomerService;
    private final int maxPeriodMillis;
    private final ScheduledExecutorService executor;

    /**
     * Constructs an instance of {@code RandomFrequentCustomerImpl}.
     *
     * @param executor                an executor to put tasks into
     * @param orderDescription        a description of performed repeated orders
     * @param pizzeriaCustomerService a pizzeria to order pizza in
     * @param maxPeriodMillis         a frequency with whom orders will be
     *                                performed by {@link #start(int)} method.
     */
    public RandomFrequentCustomerImpl(ScheduledExecutorService executor,
                                      OrderDescription orderDescription,
                                      PizzeriaCustomerService pizzeriaCustomerService,
                                      int maxPeriodMillis) {
        this.executor = executor;
        this.orderDescription = orderDescription;
        this.pizzeriaCustomerService = pizzeriaCustomerService;
        this.maxPeriodMillis = maxPeriodMillis;
    }

    /**
     * Makes an order in this {@code RandomFrequentCustomer}'s favourite
     * pizzeria.
     *
     * <p>Prints to {@link System#out} when a regular order is made,
     * either successfully or not.
     */
    @Override
    public void order() {
        pizzeriaCustomerService.makeOrder(orderDescription);
    }

    /**
     * Starts ordering a specified amount of pizzas
     * with a period that is in range between 0 and
     * {@code maxPeriodMillis}, specified in constructor.
     *
     * <p>This method reuses {@link #order()}
     * method.
     *
     * <p>All orders are processed in separated tasks
     * which are to be executed by {@code executor}
     * specified in {@link #RandomFrequentCustomerImpl(
     *ScheduledExecutorService, OrderDescription,
     * PizzeriaCustomerService, int)}
     * constructor.
     *
     * @param times amount of times to order
     */
    @Override
    public void start(int times) {
        SecureRandom rand = new SecureRandom();

        for (int i = 0; i < times; i++) {
            executor.schedule(
                this::order,
                (long) i * rand.nextLong(maxPeriodMillis),
                TimeUnit.MILLISECONDS
            );
        }
    }
}
