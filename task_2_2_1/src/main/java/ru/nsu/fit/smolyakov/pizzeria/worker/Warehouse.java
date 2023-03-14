package ru.nsu.fit.smolyakov.pizzeria.worker;

import ru.nsu.fit.smolyakov.pizzeria.entity.Order;

import java.util.List;

public interface Warehouse {
    void acceptOrderFromBaker();
    List<Order> giveOrdersToDeliveryBoy(int maxAmount);
}
