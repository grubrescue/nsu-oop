package ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOrderService;

public interface OrderInformationService {
    PizzeriaOrderService getPizzeriaOrderService();
    Order.Status getStatus();
    int getId();
}
