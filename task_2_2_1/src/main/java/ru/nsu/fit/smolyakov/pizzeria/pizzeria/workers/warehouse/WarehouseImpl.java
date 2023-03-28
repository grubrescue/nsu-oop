package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.warehouse;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaEmployeeService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.Order;
import ru.nsu.fit.smolyakov.pizzeria.util.ConsumerProducerQueue;

import java.util.Queue;

/**
 * An implementation of an {@link Warehouse} interface.
 * Reuses {@link ConsumerProducerQueue}.
 */
public class WarehouseImpl implements Warehouse {
    @JsonIgnore
    private final ConsumerProducerQueue<Order> consumerProducerQueue;
    @JsonBackReference(value = "warehouse")
    private PizzeriaEmployeeService pizzeriaStatusPrinterService;

    @JsonCreator
    private WarehouseImpl(@JsonProperty("capacity") int capacity) {
        consumerProducerQueue = new ConsumerProducerQueue<>(capacity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(Order order) {
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
