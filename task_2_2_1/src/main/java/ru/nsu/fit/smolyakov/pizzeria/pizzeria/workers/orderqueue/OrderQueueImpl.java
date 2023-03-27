package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaEmployeeService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.Order;
import ru.nsu.fit.smolyakov.pizzeria.util.ConsumerProducerQueue;

public class OrderQueueImpl implements OrderQueue {
    @JsonBackReference(value = "orderQueue")
    private PizzeriaEmployeeService pizzeriaStatusPrinterService;

    @JsonIgnore
    private final ConsumerProducerQueue<Order> consumerProducerQueue;

    @JsonIgnore
    private boolean working = false;

    @JsonCreator
    private OrderQueueImpl(@JsonProperty("capacity") int capacity) {
        consumerProducerQueue = new ConsumerProducerQueue<>(capacity);
    }

    @Override
    public void put(Order order) {
        if (!working) {
            return;
        }

        try {
            consumerProducerQueue.put(order);

            order.setStatus(Order.Status.ACCEPTED);
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
