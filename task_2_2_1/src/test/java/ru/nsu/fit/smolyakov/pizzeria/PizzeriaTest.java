package ru.nsu.fit.smolyakov.pizzeria;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import ru.nsu.fit.smolyakov.pizzeria.customer.RandomFrequentCustomerFactory;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaCustomerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaImpl;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOwnerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.Order;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.OrderInformationService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.Address;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PizzeriaTest {
    private static OrderDescription someOrderDescription(int deliveryTimeMillis) {
        return new OrderDescription(new Address("", deliveryTimeMillis), "");
    }

    @Test
    @Timeout(value = 30)
    void softStopTest() throws InterruptedException {
        PizzeriaOwnerService pizzeriaOwnerService =
            PizzeriaImpl.fromJson(Application.class.getResourceAsStream("/PizzeriaConfiguration.json"));
        PizzeriaCustomerService pizzeriaCustomerService = pizzeriaOwnerService.getOrderService();

        pizzeriaOwnerService.start();
        assertThat(pizzeriaCustomerService.isWorking()).isTrue();

        // Making some orders
        pizzeriaCustomerService.makeOrder(someOrderDescription(100));
        pizzeriaCustomerService.makeOrder(someOrderDescription(100));
        pizzeriaCustomerService.makeOrder(someOrderDescription(100));
        Optional<OrderInformationService> orderBeforeStop1 =
            pizzeriaCustomerService.makeOrder(someOrderDescription(140));
        Optional<OrderInformationService> orderBeforeStop2 =
            pizzeriaCustomerService.makeOrder(someOrderDescription(100));

        // And stopping pizzeria before they are done
        pizzeriaOwnerService.stop();
        assertThat(pizzeriaCustomerService.isWorking()).isFalse();


        // Checking if pizzeria doesn't accept new orders
        assertThat(pizzeriaCustomerService.makeOrder(someOrderDescription(666))).isEmpty();

        Thread.sleep(4000); // Wait for workers

        // Checking if pizzeria has done previous orders
        assertThat(orderBeforeStop1.get().getStatus()).isEqualTo(Order.Status.DONE);
        assertThat(orderBeforeStop2.get().getStatus()).isEqualTo(Order.Status.DONE);


        // Start pizzeria again
        pizzeriaOwnerService.start();
        assertThat(pizzeriaCustomerService.isWorking()).isTrue();

        // Again some orders
        Optional<OrderInformationService> orderAfterStop1 =
            pizzeriaCustomerService.makeOrder(someOrderDescription(140));
        Optional<OrderInformationService> orderAfterStop2 =
            pizzeriaCustomerService.makeOrder(someOrderDescription(280));
        Optional<OrderInformationService> orderAfterStop3 =
            pizzeriaCustomerService.makeOrder(someOrderDescription(10));

        Thread.sleep(5009); // Again wait for workers
        // Are they still working correctly?
        assertThat(orderAfterStop1.get().getStatus()).isEqualTo(Order.Status.DONE);
        assertThat(orderAfterStop2.get().getStatus()).isEqualTo(Order.Status.DONE);
        assertThat(orderAfterStop3.get().getStatus()).isEqualTo(Order.Status.DONE);

        // Let worker go home
        pizzeriaOwnerService.stop();
    }

    @Test
    @Timeout(value = 30)
    void forceStopTest() throws InterruptedException {
        PizzeriaOwnerService pizzeriaOwnerService =
            PizzeriaImpl.fromJson(Application.class.getResourceAsStream("/PizzeriaConfiguration.json"));
        PizzeriaCustomerService pizzeriaCustomerService = pizzeriaOwnerService.getOrderService();

        pizzeriaOwnerService.start();
        assertThat(pizzeriaCustomerService.isWorking()).isTrue();

        // Making some orders
        pizzeriaCustomerService.makeOrder(someOrderDescription(100));
        pizzeriaCustomerService.makeOrder(someOrderDescription(100));
        pizzeriaCustomerService.makeOrder(someOrderDescription(100));
        pizzeriaCustomerService.makeOrder(someOrderDescription(100));
        Optional<OrderInformationService> orderBeforeStop1 =
            pizzeriaCustomerService.makeOrder(someOrderDescription(140));
        Optional<OrderInformationService> orderBeforeStop2 =
            pizzeriaCustomerService.makeOrder(someOrderDescription(100));

        // And stopping pizzeria before they are done
        pizzeriaOwnerService.forceStop();
        assertThat(pizzeriaCustomerService.isWorking()).isFalse();

        // Checking if pizzeria doesn't accept new orders
        assertThat(pizzeriaCustomerService.makeOrder(someOrderDescription(666))).isEmpty();

        Thread.sleep(4000); // Wait for workers

        // Checking if pizzeria has forgotten previous orders (as they were cancelled)
        assertThat(orderBeforeStop1.get().getStatus()).isNotEqualTo(Order.Status.DONE);
        assertThat(orderBeforeStop2.get().getStatus()).isNotEqualTo(Order.Status.DONE);

        // Start pizzeria again
        pizzeriaOwnerService.start();
        assertThat(pizzeriaCustomerService.isWorking()).isTrue();

        // Again some orders
        Optional<OrderInformationService> orderAfterStop1 =
            pizzeriaCustomerService.makeOrder(someOrderDescription(140));
        Optional<OrderInformationService> orderAfterStop2 =
            pizzeriaCustomerService.makeOrder(someOrderDescription(280));
        Optional<OrderInformationService> orderAfterStop3 =
            pizzeriaCustomerService.makeOrder(someOrderDescription(10));

        Thread.sleep(5009); // Again wait for workers
        // Are they still working correctly?
        assertThat(orderAfterStop1.get().getStatus()).isEqualTo(Order.Status.DONE);
        assertThat(orderAfterStop2.get().getStatus()).isEqualTo(Order.Status.DONE);
        assertThat(orderAfterStop3.get().getStatus()).isEqualTo(Order.Status.DONE);

        // Let workers go home
        pizzeriaOwnerService.stop();
    }


    @Test
    @Timeout(value = 60)
    void frequentCustomerTest() throws InterruptedException {
        PizzeriaOwnerService pizzeriaOwnerService =
            PizzeriaImpl.fromJson(Application.class.getResourceAsStream("/PizzeriaConfiguration.json"));
        pizzeriaOwnerService.start();

        PizzeriaCustomerService pizzeriaCustomerService = pizzeriaOwnerService.getOrderService();
        RandomFrequentCustomerFactory randomFrequentCustomerFactory = new RandomFrequentCustomerFactory();

        randomFrequentCustomerFactory.instance(
            someOrderDescription(123),
            pizzeriaCustomerService,
            1000
        ).start(5);

        randomFrequentCustomerFactory.instance(
            someOrderDescription(356),
            pizzeriaCustomerService,
            600
        ).start(5);

        Thread.sleep(10000);

        // Покупатели в сумме сделали 10 заказов, проверим, является ли следующий 11-ым (нумерация с 0)
        assertThat(pizzeriaCustomerService.makeOrder(someOrderDescription(1)).get().getId()).isEqualTo(10);

        pizzeriaOwnerService.stop();
        randomFrequentCustomerFactory.stopCustomers();
    }
}
