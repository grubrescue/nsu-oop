package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;

public interface PizzeriaStatusPrinterService {
    void printStatus(Order order);
}
