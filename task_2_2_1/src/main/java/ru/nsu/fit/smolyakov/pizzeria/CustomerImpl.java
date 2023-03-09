package ru.nsu.fit.smolyakov.pizzeria;

import ru.nsu.fit.smolyakov.pizzeria.entity.OrderDescription;

public class CustomerImpl implements Customer {
    private OrderDescription orderDescription;
    private Pizzeria pizzeria;

    public CustomerImpl(OrderDescription orderDescription, Pizzeria pizzeria) {
        this.orderDescription = orderDescription;
        this.pizzeria = pizzeria;
    }

    @Override
    public void order() {

    }
}
