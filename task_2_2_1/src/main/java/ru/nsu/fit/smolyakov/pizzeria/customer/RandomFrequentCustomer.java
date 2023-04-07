package ru.nsu.fit.smolyakov.pizzeria.customer;

/**
 * Represents a customer, who wants to order pizza at regular or irregular intervals.
 * He probably likes pizza. Or ordering one.
 */
public interface RandomFrequentCustomer extends Customer {
    /**
     * Starts ordering a specified amount of pizzas
     * with some period.
     *
     * <p>This method is supposed to reuse {@link #order()}
     * method of {@link Customer} interface.
     *
     * @param times amount of times to order
     */
    void start(int times);
}
