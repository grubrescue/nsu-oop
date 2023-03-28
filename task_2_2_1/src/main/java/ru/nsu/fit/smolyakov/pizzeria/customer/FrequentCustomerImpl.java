package ru.nsu.fit.smolyakov.pizzeria.customer;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaCustomerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * An implementation of {@link FrequentCustomer} interface.
 * May be instantiated by
 * {@link FrequentCustomerFactory#instance(
 *OrderDescription, PizzeriaCustomerService, int)}.
 */
public class FrequentCustomerImpl implements FrequentCustomer {
    private final OrderDescription orderDescription;
    private final PizzeriaCustomerService pizzeriaCustomerService;
    private final int frequencyMillis;
    private final ScheduledExecutorService executor;

    /**
     * Constructs an instance of {@code FrequentCustomerImpl}.
     *
     * @param executor                an executor to put tasks into
     * @param orderDescription        a description of performed repeated orders
     * @param pizzeriaCustomerService a pizzeria to order pizza in
     * @param frequencyMillis         a frequency with whom orders will be
     *                                performed by {@link #start(int)} method.
     */
    public FrequentCustomerImpl(ScheduledExecutorService executor,
                                OrderDescription orderDescription,
                                PizzeriaCustomerService pizzeriaCustomerService,
                                int frequencyMillis) {
        this.executor = executor;
        this.orderDescription = orderDescription;
        this.pizzeriaCustomerService = pizzeriaCustomerService;
        this.frequencyMillis = frequencyMillis;
    }

    /**
     * Makes an order in this {@code FrequentCustomer}'s favourite
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
     * Starts ordering pizza continuously.
     *
     * <p>This method reuses {@link #order()}
     * method.
     *
     * <p>All orders are processed in separated tasks
     * which are to be executed by {@code executor}
     * specified in {@link #FrequentCustomerImpl(
     *ScheduledExecutorService, OrderDescription,
     * PizzeriaCustomerService, int)}
     * constructor.
     *
     * @param times amount of times to order
     */
    @Override
    public void start(int times) {
        for (int i = 0; i < times; i++) {
            executor.schedule(
                this::order,
                (long) i * frequencyMillis,
                TimeUnit.MILLISECONDS
            );
        }
    }
}
