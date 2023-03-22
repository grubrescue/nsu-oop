package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaStatusPrinterService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.util.ConsumerProducerQueue;

import java.beans.ConstructorProperties;

public class OrderQueueImpl implements OrderQueue {
    private final ConsumerProducerQueue<Order> consumerProducerQueue;
    private boolean working = false;

    @JsonBackReference
    private PizzeriaStatusPrinterService pizzeriaStatusPrinterService;

//    @JsonCreator
    @ConstructorProperties({"capacity"})
    public OrderQueueImpl(int capacity) {
        this.consumerProducerQueue = new ConsumerProducerQueue<>(capacity);
    }

    @Override
    public void put(Order order) {
        if (!working) {
            return;
        }

        try {
            consumerProducerQueue.put(order);
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
