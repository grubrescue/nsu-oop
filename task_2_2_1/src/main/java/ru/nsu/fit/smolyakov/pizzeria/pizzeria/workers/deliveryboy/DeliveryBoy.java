package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.deliveryboy;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = DeliveryBoyImpl.class)
public interface DeliveryBoy {
    void start();
    void forceStop();
    void stopAfterCompletion();
}
