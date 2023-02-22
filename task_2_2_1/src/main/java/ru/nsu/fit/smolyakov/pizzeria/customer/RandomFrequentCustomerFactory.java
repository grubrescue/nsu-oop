package ru.nsu.fit.smolyakov.pizzeria.customer;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaCustomerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Creates instances of {@link RandomFrequentCustomer} by
 * {@link #instance(OrderDescription, PizzeriaCustomerService, int)} method.
 *
 * <p>Used implementation ({@link RandomFrequentCustomerImpl} requires an
 * {@link java.util.concurrent.ScheduledExecutorService} to schedule tasks;
 * one factory shares one thread pool between all created customers in terms
 * of improving performance.
 *
 * <p>Instance cannot be used again after {@link #stopCustomers()} method.
 */
public class RandomFrequentCustomerFactory {
    private final ScheduledExecutorService threadPool =
        Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * Returns an instance of a {@link RandomFrequentCustomer}.
     *
     * @param orderDescription        an order description
     * @param pizzeriaCustomerService a pizzeria
     * @param frequencyMillis         a frequency
     * @return a new instance of a {@link RandomFrequentCustomer}
     */
    public RandomFrequentCustomer instance(OrderDescription orderDescription,
                                           PizzeriaCustomerService pizzeriaCustomerService,
                                           int frequencyMillis) {
        return new RandomFrequentCustomerImpl(threadPool, orderDescription,
            pizzeriaCustomerService, frequencyMillis);
    }

    /**
     * Stops all customers. Makes this factory unable to instance new customers,
     * so after using this method, new one should be instantiated.
     */
    public void stopCustomers() {
        threadPool.shutdownNow();
    }
}
