package ru.nsu.fit.smolyakov.pizzeria;

import ru.nsu.fit.smolyakov.pizzeria.entity.Order;

public interface Pizzeria {
    void start();
    void makeOrder(Order order);
}
