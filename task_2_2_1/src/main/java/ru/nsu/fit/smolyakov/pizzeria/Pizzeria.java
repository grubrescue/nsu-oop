package ru.nsu.fit.smolyakov.pizzeria;

import ru.nsu.fit.smolyakov.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.entity.OrderDescription;

public interface Pizzeria {
    void start();

    void makeOrder(OrderDescription orderDescription);

    void stop();
}
