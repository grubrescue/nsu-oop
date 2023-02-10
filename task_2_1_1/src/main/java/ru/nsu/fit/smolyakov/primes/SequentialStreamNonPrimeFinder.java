package ru.nsu.fit.smolyakov.primes;

import java.util.Arrays;

public class SequentialStreamNonPrimeFinder extends NonPrimeFinder {
    @Override
    public boolean find(int[] arr) {
        return !Arrays.stream(arr)
            .sequential()
            .allMatch(Util::isPrime);
    }
}
