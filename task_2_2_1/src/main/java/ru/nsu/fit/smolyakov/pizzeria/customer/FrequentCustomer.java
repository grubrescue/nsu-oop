package ru.nsu.fit.smolyakov.pizzeria.customer;

public interface FrequentCustomer extends Customer {
    void start(int frequencyNanos);
}
