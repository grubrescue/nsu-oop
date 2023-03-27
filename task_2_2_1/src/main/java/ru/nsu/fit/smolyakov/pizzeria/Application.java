package ru.nsu.fit.smolyakov.pizzeria;

import ru.nsu.fit.smolyakov.pizzeria.customer.FrequentCustomerFactory;
import ru.nsu.fit.smolyakov.pizzeria.customer.FrequentCustomerImpl;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaImpl;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOrderService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOwnerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.Address;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;

public class Application {
    public static void main(String[] args) {
        PizzeriaOwnerService pizzeriaOwnerService =
            PizzeriaImpl.fromJson(Application.class.getResourceAsStream("/PizzeriaConfiguration.json"));
        pizzeriaOwnerService.start();

        PizzeriaOrderService pizzeriaOrderService = pizzeriaOwnerService.getOrderService();

        FrequentCustomerFactory frequentCustomerFactory = new FrequentCustomerFactory();

        frequentCustomerFactory.instance(
            new OrderDescription(
                new Address("ПИРОГОВА 4", 2000),
                "ШАУРМА ЦЕЗАРЬЬ MAX"),
            pizzeriaOrderService,
            600
        ).start(100);

        frequentCustomerFactory.instance(
            new OrderDescription(
                new Address("ПИРОГОВА 4", 2000),
                "ШАУРМА ЦЕЗАРЬЬ MAX"),
            pizzeriaOrderService,
            50
        ).start(100);

        pizzeriaOwnerService.forceStop();
        pizzeriaOwnerService.start();
    }
}