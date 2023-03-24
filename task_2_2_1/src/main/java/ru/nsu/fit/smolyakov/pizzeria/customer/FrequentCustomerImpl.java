package ru.nsu.fit.smolyakov.pizzeria.customer;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOrderService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.OrderInformationService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;
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
        System.out.printf("(Customer %dms) Wanna order pizza... %n%n", frequencyMillis);
        pizzeriaOrderService.makeOrder(orderDescription)
            .ifPresentOrElse(
                order ->
                    System.out.printf("(Customer %dms) Pizza %d ordered! %n%n",
                        frequencyMillis, order.getId()),
                () ->
                    System.out.printf("(Customer %dms) Pizza is not ordered, as pizzeria is not working! %n%n",
                        frequencyMillis)
            );
    }

    @Override
    public void start(int times) {
        for (int i = 0; i < times; i++) {
            TasksExecutor.INSTANCE.schedule(
                this::order,
                i * frequencyMillis
            );
        }
    }
}
