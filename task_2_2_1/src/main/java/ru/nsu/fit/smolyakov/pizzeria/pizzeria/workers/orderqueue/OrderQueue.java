package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;

public interface OrderQueue {
    boolean acceptOrder(Order order);

    Order giveOrderToBaker();

    void start();
    void stop();
}
