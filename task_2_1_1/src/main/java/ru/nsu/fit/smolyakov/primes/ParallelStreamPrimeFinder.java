package ru.nsu.fit.smolyakov.primes;

import java.util.Arrays;

public class ParallelStreamPrimeFinder implements PrimeFinder {
    @Override
    public boolean find(int[] arr) {
        return Arrays.stream(arr)
            .parallel()
            .anyMatch(Util::isPrime);
    }
}
