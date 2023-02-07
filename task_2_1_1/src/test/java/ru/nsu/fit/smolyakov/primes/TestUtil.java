package ru.nsu.fit.smolyakov.primes;

import java.util.function.Function;

class TestUtil<T> {
    long measureExecutionTimeNanoseconds(Function<T, Boolean> function, T arg) {
        long startTime = System.nanoTime();
        function.apply(arg);
        long stopTime = System.nanoTime();

        return stopTime - startTime;
    }
}
