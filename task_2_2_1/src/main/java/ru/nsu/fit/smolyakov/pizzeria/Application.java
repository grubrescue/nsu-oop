package ru.nsu.fit.smolyakov.pizzeria;

import ru.nsu.fit.smolyakov.pizzeria.customer.Customer;
import ru.nsu.fit.smolyakov.pizzeria.customer.FrequentCustomer;
import ru.nsu.fit.smolyakov.pizzeria.customer.FrequentCustomerImpl;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaImpl;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOrderService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOwnerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Address;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.OrderDescription;

import java.io.File;

public class Application {
    public static void main(String[] args) {
        PizzeriaOwnerService pizzeriaOwnerService =
            PizzeriaImpl.fromJson(Application.class.getResourceAsStream("/PizzeriaConfiguration.json"));
        pizzeriaOwnerService.start();

        PizzeriaOrderService pizzeriaOrderService = pizzeriaOwnerService.getOrderService();

        new FrequentCustomerImpl(
            new OrderDescription(
                new Address("ПИРОГОВА 4", 2000),
                "ВКУСНАЯ ШАУРМА"),
            pizzeriaOrderService,
            123
        ).start(100);

    }
}