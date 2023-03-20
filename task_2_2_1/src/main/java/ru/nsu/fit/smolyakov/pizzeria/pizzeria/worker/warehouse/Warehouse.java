package ru.nsu.fit.smolyakov.pizzeria.pizzeria.worker.warehouse;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;

import java.util.List;

public interface Warehouse {
    void acceptOrderFromBaker();

    List<Order> giveOrdersToDeliveryBoy(int maxAmount);
}
