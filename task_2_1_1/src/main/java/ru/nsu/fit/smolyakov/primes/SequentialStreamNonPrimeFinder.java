package ru.nsu.fit.smolyakov.primes;

import java.util.Arrays;
import java.util.Objects;

/**
 * One of multiple implementations of {@link NonPrimeFinder}.
 * This one uses sequential {@link java.util.stream.Stream}
 * and slower than other variants with large arrays.
*/
public class SequentialStreamNonPrimeFinder implements NonPrimeFinder {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsNonPrime(int[] arr) {
        return !Arrays.stream(Objects.requireNonNull(arr))
            .sequential()
            .allMatch(Util::isPrime);
    }
}
