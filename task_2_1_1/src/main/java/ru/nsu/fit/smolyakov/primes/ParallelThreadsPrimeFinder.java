package ru.nsu.fit.smolyakov.primes;

public class ParallelThreadsPrimeFinder implements PrimeFinder {
    private final int amountOfThreads;

    public ParallelThreadsPrimeFinder(int amountOfThreads) {
        this.amountOfThreads = amountOfThreads;
    }
    @Override
    public boolean find(int[] arr) {
        return false;
    }
}
