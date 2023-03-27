package ru.nsu.fit.smolyakov.pizzeria.util;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

public class ConsumerProducerQueue<T> {
    private final Queue<T> queue;
    private final int capacity;

    public ConsumerProducerQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new ArrayDeque<>(capacity);
    }

    public synchronized void put(T item) throws InterruptedException {
        if (Objects.isNull(item)) {
            throw new NullPointerException();
        }

        while (queue.size() == capacity) {
            wait();
        }

        queue.add(item);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (queue.size() == 0) {
            wait();
        }
        var polledValue = queue.poll();
        notifyAll();
        return polledValue;
    }

    public synchronized Queue<T> takeMultiple(int amount) throws InterruptedException {
        if (amount < 1) {
            throw new IllegalArgumentException("amount should be at least 1");
        }

        while (queue.size() == 0) {
            wait();
        }

        amount = Integer.min(amount, queue.size());
        Queue<T> resultQueue = new ArrayDeque<>(amount);

        while (resultQueue.size() < amount) {
            resultQueue.add(queue.poll());
        }

        notifyAll();
        return resultQueue;
    }

    public synchronized void clear() {
        queue.clear();
    }
}
