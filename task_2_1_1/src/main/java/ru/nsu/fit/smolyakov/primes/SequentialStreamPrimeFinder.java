package ru.nsu.fit.smolyakov.primes;

import java.util.Arrays;

public class SequentialStreamPrimeFinder implements PrimeFinder {
    @Override
    public boolean find(int[] arr) {
        return Arrays.stream(arr)
            .anyMatch(Util::isPrime);
    }
}
