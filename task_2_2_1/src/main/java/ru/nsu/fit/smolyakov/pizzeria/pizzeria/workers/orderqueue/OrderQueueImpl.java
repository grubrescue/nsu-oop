package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaEmployeeService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.Order;
import ru.nsu.fit.smolyakov.pizzeria.util.ConsumerProducerQueue;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An implementation of an {@link OrderQueue} interface.
 * Reuses {@link ConsumerProducerQueue}.
 */
public class OrderQueueImpl implements OrderQueue {
    @JsonIgnore
    private final ConsumerProducerQueue<Order> consumerProducerQueue;

    @JsonBackReference(value = "orderQueue")
    private PizzeriaEmployeeService pizzeriaStatusPrinterService;

    @JsonCreator
    private OrderQueueImpl(@JsonProperty("capacity") int capacity) {
        consumerProducerQueue = new ConsumerProducerQueue<>(capacity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(Order order) {
        try {
            consumerProducerQueue.put(order);

            order.setStatus(Order.Status.ACCEPTED);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Order take() {
        try {
            return consumerProducerQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forceStop() {
        consumerProducerQueue.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return consumerProducerQueue.isEmpty();
    }
}
