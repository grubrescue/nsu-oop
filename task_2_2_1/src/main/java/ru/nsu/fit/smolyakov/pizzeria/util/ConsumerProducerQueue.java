package ru.nsu.fit.smolyakov.pizzeria.util;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

/**
 * Less feature-rich implementation of {@link java.util.concurrent.BlockingQueue}.
 * Provides an implementation of a self-blocking FIFO queue with a limited capacity.
 *
 * @param <T> T
 */
public class ConsumerProducerQueue<T> {
    private final Queue<T> queue;
    private final int capacity;

    /**
     * Creates a {@code ConsumerProducerQueue} with a specified
     * {@code capacity}.
     *
     * @param capacity a capacity
     */
    public ConsumerProducerQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new ArrayDeque<>(capacity);
    }

    /**
     * Puts an {@code item} into a tail of a queue. Blocks until one is not full.
     *
     * @param item a specified item
     * @throws InterruptedException if current thread was interrupted on block
     */
    public synchronized void put(T item) throws InterruptedException {
        if (Objects.isNull(item)) {
            throw new NullPointerException("null items are not allowed");
        }

        while (queue.size() == capacity) {
            wait();
        }

        queue.add(item);
        notifyAll();
    }

    /**
     * Returns an item from the head of a queue. Blocks until
     * queue is not empty.
     *
     * @return an item from a queue
     * @throws InterruptedException if current thread was interrupted on block
     */
    public synchronized T take() throws InterruptedException {
        while (queue.size() == 0) {
            wait();
        }
        var polledValue = queue.poll();
        notifyAll();
        return polledValue;
    }

    /**
     * Returns a queue of items from the head of a queue. Blocks until
     * queue is not empty.
     *
     * @param maxAmount a maximum amount of items to take
     * @return a queue of items
     * @throws InterruptedException     if current thread was interrupted on block
     * @throws IllegalArgumentException if a specified {@code amount} is less than 1
     */
    public synchronized Queue<T> takeMultiple(int maxAmount) throws InterruptedException {
        if (maxAmount < 1) {
            throw new IllegalArgumentException("amount should be at least 1");
        }

        while (queue.size() == 0) {
            wait();
        }

        var amount = Integer.min(maxAmount, queue.size());
        Queue<T> resultQueue = new ArrayDeque<>(amount);

        while (resultQueue.size() < amount) {
            resultQueue.add(queue.poll());
        }

        notifyAll();
        return resultQueue;
    }

    /**
     * Clears the queue.
     */
    public synchronized void clear() {
        queue.clear();
        notifyAll();
    }

    /**
     * Returns {@code true} if this queue is empty.
     *
     * @return {@code true} if this queue is empty
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
