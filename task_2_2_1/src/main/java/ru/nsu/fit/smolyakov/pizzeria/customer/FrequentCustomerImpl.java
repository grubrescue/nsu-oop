package ru.nsu.fit.smolyakov.pizzeria.customer;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOrderService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.OrderDescription;
import ru.nsu.fit.smolyakov.pizzeria.util.TaskExecutor;

public class FrequentCustomerImpl implements FrequentCustomer {
    private final OrderDescription orderDescription;
    private final PizzeriaOrderService pizzeriaOrderService;
    private final int frequencyMillis;

    public FrequentCustomerImpl(OrderDescription orderDescription,
                                PizzeriaOrderService pizzeriaOrderService,
                                int frequencyMillis) {
        this.orderDescription = orderDescription;
        this.pizzeriaOrderService = pizzeriaOrderService;
        this.frequencyMillis = frequencyMillis;
    }



    @Override
    public void order() {
        pizzeriaOrderService.makeOrder(orderDescription);
    }

    @Override
    public void start(int times) {
        Runnable task =
            () -> {
                for (int i = 0; i < times; i++) {
                    order();
                    try {
                        Thread.sleep(frequencyMillis);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            };

        TaskExecutor.INSTANCE.execute(task);
    }
}
