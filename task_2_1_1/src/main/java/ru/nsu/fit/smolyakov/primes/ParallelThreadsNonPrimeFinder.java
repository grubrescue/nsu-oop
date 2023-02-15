package ru.nsu.fit.smolyakov.primes;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelThreadsNonPrimeFinder extends NonPrimeFinder {
    private static class IntArrayIterator implements PrimitiveIterator.OfInt {
        private int i = -1;
        private final int[] arr;

        public IntArrayIterator(int[] arr) {
            this.arr = arr;
        }

        @Override
        public boolean hasNext() {
            return i < arr.length;
        }

        @Override
        public int nextInt() {
            return arr[i++];
        }
    }

    private final int amountOfThreads;

    public ParallelThreadsNonPrimeFinder(int amountOfThreads) {
        super();

        if (amountOfThreads <= 0) {
            throw new IllegalArgumentException();
        }
        this.amountOfThreads = amountOfThreads;
    }

    void threadTask(Thread predecessor, Thread prev, int[] arr, AtomicInteger iter) {
        int i = iter.getAndIncrement();
        while (i < arr.length) {
            if (!Util.isPrime(arr[i])) {
                predecessor.interrupt();
                return;
            }

            i = iter.getAndIncrement();
        }

        try {
            if (prev != null) {
                prev.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean find(int[] arr) {
        List<Thread> threadsList = new ArrayList<>();
        var realAmountOfThreads = Integer.min(amountOfThreads, Integer.max(arr.length/4, 1));

        AtomicInteger iter = new AtomicInteger(0);

        for (int threadId = 0; threadId < realAmountOfThreads; threadId++) {
            final var currentThread = Thread.currentThread();
            threadsList.add(new Thread(() -> threadTask(currentThread, null, arr, iter)));
        }

        threadsList.forEach(Thread::start);

        boolean res = false;

        try {
            threadsList.get(0).join();
        } catch (InterruptedException e) {
            System.err.println("asdf");
            res = true;
        }

        threadsList.forEach(Thread::interrupt);

        return res;
    }
}
