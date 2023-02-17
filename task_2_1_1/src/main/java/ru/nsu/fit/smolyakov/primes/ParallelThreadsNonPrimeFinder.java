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
        if (arr == null) {
            throw new IllegalArgumentException();
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
