package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue;

import com.fasterxml.jackson.annotation.*;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaStatusPrinterService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.util.ConsumerProducerQueue;

import java.beans.ConstructorProperties;

public class OrderQueueImpl implements OrderQueue {
    private final ConsumerProducerQueue<Order> consumerProducerQueue;
    private boolean working = false;

    @JsonBackReference
    private PizzeriaStatusPrinterService pizzeriaStatusPrinterService;

    @ConstructorProperties({"capacity", "pizzeriaStatusPrinterService"})
    public OrderQueueImpl(int capacity, PizzeriaStatusPrinterService owner) {
        this.pizzeriaStatusPrinterService = owner;
        this.consumerProducerQueue = new ConsumerProducerQueue<>(capacity);
    }

    @Override
    public void put(Order order) {
        if (!working) {
            return;
        }

        System.out.println("puttingorder");
        System.out.println(pizzeriaStatusPrinterService);

        try {
            consumerProducerQueue.put(order);
            pizzeriaStatusPrinterService.printStatus(order);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order take() {
        try {
            return consumerProducerQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void start() {
        working = true;
    }

    @Override
    public synchronized void stop() {
        working = false;
    }
}
