package ru.nsu.fit.smolyakov.primes;

/**
 * An abstract class, whose predecessors has to provide
 * an implementation {@link #containsNonPrime(int[])} method. Latter does the job
 * checking if there is at least one non-prime number in
 * an array of {@link Integer} primitives.
 */
public interface NonPrimeFinder {
    /**
     * Returns if a specified {@code arr} contains at
     * least one non-prime number.
     *
     * @param arr a specified array of {@link Integer} primitives
     * @return {@code true} if a specified {@code arr}
     * contains at least one non-prime number
     * @throws NullPointerException if {@code arr} is null
     */
    boolean containsNonPrime(int[] arr);
}
