package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.OrderDescription;

public interface PizzeriaOrderService {
    String getPizzeriaName();
    boolean isWorking();
    boolean makeOrder(OrderDescription orderDescription);
}
