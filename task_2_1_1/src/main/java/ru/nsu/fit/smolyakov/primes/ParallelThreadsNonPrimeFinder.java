package ru.nsu.fit.smolyakov.primes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * One of multiple implementations of {@link NonPrimeFinder}.
 * This one uses self-written algorithm with only use of basic
 * {@link Thread} methods and {@link AtomicInteger} variables,
 * without {@code synchronized} blocks and mutexes/semaphores,
 * so performance might be not surprisingly great.
 */
public class ParallelThreadsNonPrimeFinder implements NonPrimeFinder {
    private final int amountOfThreads;

    /**
     * Creates an instance of {@code ParallelThreadsNonPrimeFinder}
     * with a specified {@code amountOfThreads} that will be
     * created by {@link #containsNonPrime(int[])}.
     *
     * @param amountOfThreads a specified amount of threads
     *                        that will be used by
     *                        {@link #containsNonPrime(int[])}
     *                        method.
     */
    public ParallelThreadsNonPrimeFinder(int amountOfThreads) {
        if (amountOfThreads < 1) {
            throw new IllegalArgumentException();
        }
        this.amountOfThreads = amountOfThreads;
    }

    private void threadTask(Thread predecessor, int[] arr, AtomicInteger iter) {
        int i = iter.getAndIncrement();
        while (!Thread.interrupted() && i < arr.length) {
            if (!Util.isPrime(arr[i])) {
                predecessor.interrupt();
                return;
            }

            i = iter.getAndIncrement();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsNonPrime(int[] arr) {
        if (arr == null) {
            throw new NullPointerException();
        }

        List<Thread> threadsList = new ArrayList<>();

        final AtomicInteger iter = new AtomicInteger(0);
        final var currentThread = Thread.currentThread();
        currentThread.setPriority(Thread.MAX_PRIORITY);

        for (int threadId = 0; threadId < amountOfThreads - 1; threadId++) {
            threadsList.add(new Thread(() -> threadTask(currentThread, arr, iter)));
        }

        threadsList.add(
            new Thread(() -> {
                threadTask(currentThread, arr, iter);

                for (int threadId = 0; threadId < amountOfThreads - 1; threadId++) {
                    var thread = threadsList.get(threadId);
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
            )
        );

        boolean res = false;
        try {
            threadsList.forEach(Thread::start);
            threadsList.get(amountOfThreads - 1).join();
        } catch (InterruptedException e) {
            res = true;
            threadsList.forEach(Thread::interrupt);
        }

        return res;
    }
}
