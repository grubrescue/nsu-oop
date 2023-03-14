package ru.nsu.fit.smolyakov.pizzeria;

import ru.nsu.fit.smolyakov.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.entity.OrderDescription;

public interface Pizzeria {
    void start();
    boolean makeOrder(OrderDescription orderDescription);
    void printOrderStatus(Order order);
    void stop();
}
