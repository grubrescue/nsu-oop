package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.OrderDescription;

public interface PizzeriaOrderService {
    boolean makeOrder(OrderDescription orderDescription);
}
