package ru.nsu.fit.smolyakov.primes;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelThreadsNonPrimeFinder extends NonPrimeFinder {
    private final int amountOfThreads;

    public ParallelThreadsNonPrimeFinder(int amountOfThreads) {
        super();

        if (amountOfThreads <= 0) {
            throw new IllegalArgumentException();
        }
        this.amountOfThreads = amountOfThreads;
    }

    void threadTask(Thread predecessor, int[] arr, AtomicInteger iter) {
        int i = iter.getAndIncrement();
        while (i < arr.length) {
            if (!Util.isPrime(arr[i])) {
                predecessor.interrupt();
                return;
            }

            i = iter.getAndIncrement();
        }
    }

    @Override
    public boolean find(int[] arr) {
        List<Thread> threadsList = new ArrayList<>();

        final var realAmountOfThreads = Integer.min(amountOfThreads, Integer.max(arr.length/4, 1));

        final AtomicInteger iter = new AtomicInteger(0);
        final var currentThread = Thread.currentThread();

        for (int threadId = 0; threadId < realAmountOfThreads - 1; threadId++) {
            threadsList.add(new Thread(() -> threadTask(currentThread, arr, iter)));
        }

        threadsList.add(new Thread(() -> {
            threadTask(currentThread, arr, iter);

            for (int threadId = 0; threadId < realAmountOfThreads - 1; threadId++) {
                var thread = threadsList.get(threadId);
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }));

        threadsList.forEach(Thread::start);

        boolean res = false;

        try {
            threadsList.get(realAmountOfThreads - 1).join();
        } catch (InterruptedException e) {
            res = true;
            threadsList.forEach(Thread::interrupt);
        }

        return res;
    }
}
