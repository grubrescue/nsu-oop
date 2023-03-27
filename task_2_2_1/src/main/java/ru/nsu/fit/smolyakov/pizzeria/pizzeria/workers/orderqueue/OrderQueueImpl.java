package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaEmployeeService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.Order;
import ru.nsu.fit.smolyakov.pizzeria.util.ConsumerProducerQueue;

import java.util.concurrent.atomic.AtomicBoolean;

public class OrderQueueImpl implements OrderQueue {
    @JsonBackReference(value = "orderQueue")
    private PizzeriaEmployeeService pizzeriaStatusPrinterService;

    @JsonIgnore
    private final ConsumerProducerQueue<Order> consumerProducerQueue;

    @JsonIgnore
    private final AtomicBoolean working = new AtomicBoolean(false);

    @JsonCreator
    private OrderQueueImpl(@JsonProperty("capacity") int capacity) {
        consumerProducerQueue = new ConsumerProducerQueue<>(capacity);
    }

    @Override
    public void put(Order order) {
        if (!working.get()) {
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
    public void start() {
        working.set(true);
    }

    @Override
    public void forceStop() {
        consumerProducerQueue.clear();
        working.set(false);
    }

    @Override
    public void stopAfterCompletion() {
        try {
            consumerProducerQueue.waitUntilEmpty();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        working.set(false);
    }
}
