package ru.nsu.fit.smolyakov.pizzeria.worker;

public interface OrderQueue {
    void addOrder();
    void pollOrder();
}
