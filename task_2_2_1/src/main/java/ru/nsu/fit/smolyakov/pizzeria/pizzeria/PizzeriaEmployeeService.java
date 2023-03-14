package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;

public interface PizzeriaEmployeeService {
    void printOrderStatus(Order order);
    void submitTask(Runnable task);
}
