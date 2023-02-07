package ru.nsu.fit.smolyakov.primes;

import java.util.Arrays;
import java.util.Objects;

/**
 * One of multiple implementations of {@link NonPrimeFinder}.
 * This one uses parallel {@link java.util.stream.Stream},
 * thus, irrational overhead occurs when using with small
 * arrays.
 */
public class ParallelStreamNonPrimeFinder implements NonPrimeFinder {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsNonPrime(int[] arr) {
        return !Arrays.stream(Objects.requireNonNull(arr))
            .parallel()
            .allMatch(Util::isPrime);
    }
}
