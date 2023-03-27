package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.warehouse;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaEmployeeService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.Order;
import ru.nsu.fit.smolyakov.pizzeria.util.ConsumerProducerQueue;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;


public class WarehouseImpl implements Warehouse {
    @JsonBackReference(value = "warehouse")
    private PizzeriaEmployeeService pizzeriaStatusPrinterService;

    @JsonIgnore
    private final ConsumerProducerQueue<Order> consumerProducerQueue;

    @JsonIgnore
    private final AtomicBoolean working = new AtomicBoolean(false);

    @JsonCreator
    private WarehouseImpl(@JsonProperty("capacity") int capacity) {
        consumerProducerQueue = new ConsumerProducerQueue<>(capacity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(Order order) {
        if (!working.get()) {
            return;
        }

        try {
            consumerProducerQueue.put(order);

            order.setStatus(Order.Status.WAITING_FOR_DELIVERY);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Queue<Order> takeMultiple(int maxAmount) {
        try {
            return consumerProducerQueue.takeMultiple(maxAmount);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        working.set(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forceStop() {
        working.set(false);
        consumerProducerQueue.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopAfterCompletion() {
        working.set(false);
        try {
            consumerProducerQueue.waitUntilEmpty();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
