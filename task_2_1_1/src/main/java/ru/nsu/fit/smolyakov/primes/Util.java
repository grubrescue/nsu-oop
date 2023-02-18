package ru.nsu.fit.smolyakov.primes;

/**
 * Contains static utility method {@link #isPrime(int)},
 * shared between all implementations of
 * {@link NonPrimeFinder}.
 */
public class Util {
    /**
     * Returns if the specified {@code number} is prime.
     * @param number a number
     * @return {@code true} if the specified {@code number}
     *      is prime.
     */
    public static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }

        for (int i = 2; i * i <= number; i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }
}
