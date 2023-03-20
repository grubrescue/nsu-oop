package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;

public interface OrderQueue {
    void put(Order order);
    Order take();

    void start();
    void stop();
}
