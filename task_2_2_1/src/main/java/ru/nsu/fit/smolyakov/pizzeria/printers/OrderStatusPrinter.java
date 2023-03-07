package ru.nsu.fit.smolyakov.pizzeria.printers;

import ru.nsu.fit.smolyakov.pizzeria.entity.Order;

public interface OrderStatusPrinter {
    void print(Order order);
}
