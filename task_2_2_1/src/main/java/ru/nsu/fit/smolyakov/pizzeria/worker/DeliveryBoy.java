package ru.nsu.fit.smolyakov.pizzeria.worker;

public interface DeliveryBoy {
    void takeOrdersFromWarehouse();
    Order deliverOrder();
}
