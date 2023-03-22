package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue;

import com.fasterxml.jackson.annotation.*;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaStatusPrinterService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.util.ConsumerProducerQueue;

import java.beans.ConstructorProperties;

public class OrderQueueImpl implements OrderQueue {
    @JsonBackReference(value = "orderQueue")
    private PizzeriaStatusPrinterService pizzeriaStatusPrinterService;

    @JsonProperty("capacity")
    private int capacity;

    @JsonIgnore
    private final ConsumerProducerQueue<Order> consumerProducerQueue = new ConsumerProducerQueue<>(capacity);

    @JsonIgnore
    private boolean working = false;

    private OrderQueueImpl() {};

    @Override
    public void put(Order order) {
        if (!working) {
            return;
        }

        System.out.println("puttingorder");

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
