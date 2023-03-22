package ru.nsu.fit.smolyakov.pizzeria.customer;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOrderService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.OrderDescription;
import ru.nsu.fit.smolyakov.pizzeria.util.TasksExecutor;

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
                    try {
                        Thread.sleep(frequencyMillis);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    final var index = i;
                    TasksExecutor.INSTANCE.execute(() ->
                        {
                            System.out.printf("(customer %dms) pizza %d ordered %n%n", frequencyMillis, index);
                            order();
                            System.out.printf("(customer %dms) pizza %d recieved %n%n", frequencyMillis, index);
                        }
                    );
                }
            };

        TasksExecutor.INSTANCE.execute(task);
    }
}
