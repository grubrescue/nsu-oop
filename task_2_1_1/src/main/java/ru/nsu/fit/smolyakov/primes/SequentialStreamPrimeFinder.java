package ru.nsu.fit.smolyakov.primes;

import java.util.Arrays;

public class SequentialStreamPrimeFinder extends PrimeFinder {
    @Override
    public boolean find(int[] arr) {
        return Arrays.stream(arr)
            .sequential()
            .anyMatch(Util::isPrime);
    }
}
