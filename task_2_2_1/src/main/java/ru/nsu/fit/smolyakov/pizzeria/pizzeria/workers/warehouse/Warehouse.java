package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.warehouse;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;

import java.util.List;

public interface Warehouse {
    void put(Order order);
    List<Order> takeMultiple(int maxAmount);
}
