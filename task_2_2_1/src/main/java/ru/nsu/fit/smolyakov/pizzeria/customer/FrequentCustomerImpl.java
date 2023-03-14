package ru.nsu.fit.smolyakov.pizzeria.customer;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOrderService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.OrderDescription;

public class FrequentCustomerImpl implements FrequentCustomer {
    private final OrderDescription orderDescription;
    private final PizzeriaOrderService pizzeria;

    public FrequentCustomerImpl(OrderDescription orderDescription, PizzeriaOrderService pizzeria) {
        this.orderDescription = orderDescription;
        this.pizzeria = pizzeria;
    }

    @Override
    public void order() {
        pizzeria.makeOrder(orderDescription);
    }

    @Override
    public void start(int frequencyNanos) {
        this.order();
    }
}
