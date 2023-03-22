package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = PizzeriaImpl.class)
public interface PizzeriaOwnerService {
    void start();

    boolean isWorking();

    void stop();

    PizzeriaOrderService getOrderService();
}
