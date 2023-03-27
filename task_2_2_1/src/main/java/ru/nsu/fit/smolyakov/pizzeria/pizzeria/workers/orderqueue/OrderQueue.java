package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.Order;

/**
 * Represents an FIFO order queue in a pizzeria. Allows to
 * {@link #put(Order)}} awaiting orders in it and
 * {@link #take()} ones from it.
 */
@JsonDeserialize(as = OrderQueueImpl.class)
public interface OrderQueue {
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
     * Takes an order from the queue.
     *
     * <p>This operation is blocking: if queue is empty,
     * execution stops until previous statement won't become false.
     *
     * @return an order from the top of the queue
     */
    Order take();

    /**
     * Initiates the queue.
     */
    void start();

    /**
     * Removes all contained orders from the queue
     * and lets the reception go home right now.
     */
    void forceStop();

    /**
     * Allows the reception go home. It is supposed that
     * all other workers will be serving remaining orders
     * before they also go home.
     */
    void stopAfterCompletion();
}
