package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.warehouse;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue.OrderQueueImpl;

import java.util.List;
import java.util.Queue;

@JsonDeserialize(as = WarehouseImpl.class)
public interface Warehouse {
    void put(Order order);

    Queue<Order> takeMultiple(int maxAmount);
}
