package ru.nsu.fit.smolyakov.primes;


import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class PrimeFinderTests {
    @Test
    void sequentialStreamNonPrimeFinderTest() {
        assertThat(TestSources.sequentialStreamNonPrimeFinder.find(TestSources.smallestArrayTrue)).isTrue();
        assertThat(TestSources.sequentialStreamNonPrimeFinder.find(TestSources.smallArrayFalse)).isFalse();
        assertThat(TestSources.sequentialStreamNonPrimeFinder.find(TestSources.largeArrayTrue)).isTrue();
    }

    @Test
    void parallelStreamNonPrimeFinderTest() {
        assertThat(TestSources.parallelStreamNonPrimeFinder.find(TestSources.smallestArrayTrue)).isTrue();
        assertThat(TestSources.parallelStreamNonPrimeFinder.find(TestSources.smallArrayFalse)).isFalse();
        assertThat(TestSources.parallelStreamNonPrimeFinder.find(TestSources.largeArrayTrue)).isTrue();
    }

    @RepeatedTest(30)
    void parallelThreadsNonPrimeFinderTest() {
        assertThat(TestSources.parallelThreadsNonPrimeFinder.find(TestSources.smallestArrayTrue)).isTrue();
        assertThat(TestSources.parallelThreadsNonPrimeFinder.find(TestSources.smallArrayFalse)).isFalse();
        assertThat(TestSources.parallelThreadsNonPrimeFinder.find(TestSources.largeArrayTrue)).isTrue();
    }

}
