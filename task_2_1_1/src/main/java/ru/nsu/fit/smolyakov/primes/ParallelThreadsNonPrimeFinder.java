package ru.nsu.fit.smolyakov.primes;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.concurrent.atomic.AtomicBoolean;

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
        this.amountOfThreads = amountOfThreads;
    }


    public void threadTask(
        final Thread parentThread,
        final IntArrayIterator iter,
        final AtomicBoolean foundFlag
    ) {
        while (true) {
            final int number;

            synchronized (iter) {
                if (iter.hasNext()) {
                    number = iter.next();
                } else {
//                    Thread.currentThread().interrupt();
                    return;
                }
            }

            if (!Util.isPrime(number)) {
                synchronized (this) {
                    foundFlag.set(true);
                }
                return;
            }
        }
    }

    @Override
    public boolean find(int[] arr) {
        List<Thread> threadsList = new ArrayList<>();

        final var iter = new IntArrayIterator(arr);
        AtomicBoolean foundFlag = new AtomicBoolean(false);

        for (int threadId = 0; threadId < amountOfThreads; threadId++) {
            threadsList.add(new Thread(() -> threadTask(Thread.currentThread(), iter, foundFlag)));
        }

        synchronized (foundFlag) {
            try {
                foundFlag.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        threadsList.forEach(Thread::interrupt);

        return foundFlag.get();
    }
}
