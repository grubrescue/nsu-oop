package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.warehouse;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;

import java.util.List;
import java.util.Queue;

public interface Warehouse {
    void put(Order order);

    Queue<Order> takeMultiple(int maxAmount);
}
