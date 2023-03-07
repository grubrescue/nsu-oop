package ru.nsu.fit.smolyakov.pizzeria.worker;

public interface Baker {
    void acceptOrderFromQueue();
    void putOrderToWarehouse();
}
