package ru.nsu.fit.smolyakov.primes;

import java.util.Arrays;

public class ParallelStreamNonPrimeFinder extends NonPrimeFinder {
    @Override
    public boolean find(int[] arr) {
        return !Arrays.stream(arr)
            .parallel()
            .allMatch(Util::isPrime);
    }
}
