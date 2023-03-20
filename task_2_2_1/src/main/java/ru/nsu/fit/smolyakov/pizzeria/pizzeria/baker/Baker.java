package ru.nsu.fit.smolyakov.pizzeria.pizzeria.baker;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;

public interface Baker {
    void acceptOrderFromQueue();
    Order putOrderToWarehouse();
}
