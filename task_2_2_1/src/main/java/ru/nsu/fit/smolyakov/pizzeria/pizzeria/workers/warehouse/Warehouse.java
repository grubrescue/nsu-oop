package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.warehouse;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.Order;

import java.util.Queue;

/**
 * Represents an FIFO warehouse in a pizzeria. Allows to
 * {@link #put(Order)}} awaiting orders in it and
 * {@link #takeMultiple(int)} items from one.
 *
 * <p>Can be started, stopped or force stopped.
 */
@JsonDeserialize(as = WarehouseImpl.class)
public interface Warehouse {
    /**
     * Puts an order in a queue.
     *
     * <p>This operation is blocking: if queue capacity is exhausted,
     * execution stops until previous statement won't become false.
     *
     * @param order a specified order to put into queue
     */
    void put(Order order);

    /**
     * Takes some orders from the queue.
     *
     * <p>This operation is blocking: if queue is empty,
     * execution stops until previous statement won't become false.
     *
     * @param maxAmount a maximum amount of pizzas
     *                  a delivery guy can bring with him
     *                  (usually limited by his trunk capacity)
     * @return an order from the top of the queue
     * @throws IllegalArgumentException if {@code maxAmount}
     *                                  is less than 1.
     */
    Queue<Order> takeMultiple(int maxAmount);

    /**
     * Initiates the warehouse.
     */
    void start();

    /**
     * Removes all contained orders from the warehouse
     * and lets the reception go home right now.
     */
    void forceStop();

    /**
     * Allows the warehouse workers go home. It is supposed that
     * delivery boys will deliver all pizzas before they also
     * will be able to go home.
     */
    void stopAfterCompletion();

    /**
     * Returns if there are more orders in a warehouse.
     *
     * @return {@code true} if there are more orders in a warehouse
     */
    boolean isEmpty();
}
