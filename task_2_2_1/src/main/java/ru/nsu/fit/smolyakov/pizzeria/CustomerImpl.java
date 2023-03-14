package ru.nsu.fit.smolyakov.pizzeria;

import ru.nsu.fit.smolyakov.pizzeria.entity.OrderDescription;

public class CustomerImpl implements Customer {
    private final OrderDescription orderDescription;
    private final Pizzeria pizzeria;

    public CustomerImpl(OrderDescription orderDescription, Pizzeria pizzeria) {
        this.orderDescription = orderDescription;
        this.pizzeria = pizzeria;
    }

    @Override
    public void order() {
        pizzeria.makeOrder(orderDescription);
    }
}
