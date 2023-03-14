package ru.nsu.fit.smolyakov.pizzeria.worker;

import ru.nsu.fit.smolyakov.pizzeria.entity.Order;

public interface Baker {
    void acceptOrderFromQueue();
    Order putOrderToWarehouse();
}
