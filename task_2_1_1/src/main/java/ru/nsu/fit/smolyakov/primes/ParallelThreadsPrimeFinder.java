package ru.nsu.fit.smolyakov.primes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParallelThreadsPrimeFinder extends PrimeFinder {
    private final int amountOfThreads;

    public ParallelThreadsPrimeFinder(int amountOfThreads) {
        super();
        this.amountOfThreads = amountOfThreads;
    }

    private

    @Override
    public boolean find(int[] arr) {
        AtomicBoolean foundPrime = new AtomicBoolean(false);
        List<Thread> threadsList = new ArrayList<>();

        for (int threadId = 0; threadId < amountOfThreads; threadId++) {
            int finalThreadId = threadId;
            threadsList.add(
                new Thread(
                    () -> {
                        for (int i = finalThreadId; i < arr.length; i += amountOfThreads) {
                            if (Util.isPrime(arr[i])) {
                                foundPrime.set(true);
                            }
                        }
                    }
                    )
            );
        }

        return foundPrime.get();
    }
}
