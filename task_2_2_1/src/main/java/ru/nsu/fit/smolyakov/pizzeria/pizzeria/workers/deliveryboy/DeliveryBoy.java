package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.deliveryboy;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue.OrderQueueImpl;

@JsonDeserialize(as = DeliveryBoyImpl.class)
public interface DeliveryBoy {
    void deliver();
}
