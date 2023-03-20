package ru.nsu.fit.smolyakov.pizzeria.customer;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOrderService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.OrderDescription;

public class FrequentCustomerImpl implements FrequentCustomer {
    private final OrderDescription orderDescription;
    private final PizzeriaOrderService pizzeriaOrderService;
    private final Thread thread;

    public FrequentCustomerImpl(OrderDescription orderDescription,
                                PizzeriaOrderService pizzeriaOrderService,
                                int frequencyMillis) {
        this.orderDescription = orderDescription;
        this.pizzeriaOrderService = pizzeriaOrderService;

        this.thread = new Thread(() -> {
            try {
                order();
                Thread.sleep(frequencyMillis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void order() {
        pizzeriaOrderService.makeOrder(orderDescription);
    }

    @Override
    public void start() {
        this.thread.start();
    }
}
