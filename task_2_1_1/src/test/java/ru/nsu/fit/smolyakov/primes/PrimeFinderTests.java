package ru.nsu.fit.smolyakov.primes;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class PrimeFinderTests {
    @Test
    void sequentialStreamNonPrimeFinderTest() {
        assertThat(TestSources.sequentialStreamNonPrimeFinder.containsNonPrime(TestSources.smallestArrayTrue)).isTrue();
        assertThat(TestSources.sequentialStreamNonPrimeFinder.containsNonPrime(TestSources.smallArrayFalse)).isFalse();
        assertThat(TestSources.sequentialStreamNonPrimeFinder.containsNonPrime(TestSources.largeArrayTrue)).isTrue();
    }

    @Test
    void parallelStreamNonPrimeFinderTest() {
        assertThat(TestSources.parallelStreamNonPrimeFinder.containsNonPrime(TestSources.smallestArrayTrue)).isTrue();
        assertThat(TestSources.parallelStreamNonPrimeFinder.containsNonPrime(TestSources.smallArrayFalse)).isFalse();
        assertThat(TestSources.parallelStreamNonPrimeFinder.containsNonPrime(TestSources.largeArrayTrue)).isTrue();
    }

    @Test
    void parallelThreadsNonPrimeFinderTest() {
        assertThat(TestSources.parallelThreadsNonPrimeFinder.containsNonPrime(TestSources.smallestArrayTrue)).isTrue();
        assertThat(TestSources.parallelThreadsNonPrimeFinder.containsNonPrime(TestSources.smallArrayFalse)).isFalse();
        assertThat(TestSources.parallelThreadsNonPrimeFinder.containsNonPrime(TestSources.largeArrayTrue)).isTrue();
    }

}
