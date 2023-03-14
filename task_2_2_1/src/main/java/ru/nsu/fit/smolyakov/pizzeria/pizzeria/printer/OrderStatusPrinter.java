package ru.nsu.fit.smolyakov.pizzeria.pizzeria.printer;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;

public interface OrderStatusPrinter {
    void print(Order order);
}
