package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.Order;

@JsonDeserialize(as = OrderQueueImpl.class)
public interface OrderQueue {
    void put(Order order);

    Order take();

    void start();

    void stop();

    void forceStop();
}
