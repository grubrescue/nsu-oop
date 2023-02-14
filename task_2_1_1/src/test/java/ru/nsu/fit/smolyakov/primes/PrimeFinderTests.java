package ru.nsu.fit.smolyakov.primes;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class PrimeFinderTests {
    @Test
    void sequentialStreamNonPrimeFinderTest() {
        assertThat(TestSources.sequentialStreamNonPrimeFinder.find(TestSources.smallestArray)).isTrue();
        assertThat(TestSources.sequentialStreamNonPrimeFinder.find(TestSources.smallArray)).isFalse();
        assertThat(TestSources.sequentialStreamNonPrimeFinder.find(TestSources.largeArray)).isFalse();
    }

    @Test
    void parallelStreamNonPrimeFinderTest() {
        assertThat(TestSources.parallelStreamNonPrimeFinder.find(TestSources.smallestArray)).isTrue();
        assertThat(TestSources.parallelStreamNonPrimeFinder.find(TestSources.smallArray)).isFalse();
        assertThat(TestSources.parallelStreamNonPrimeFinder.find(TestSources.largeArray)).isFalse();
    }

    @Test
    void parallelThreadsNonPrimeFinderTest() {
        assertThat(TestSources.parallelThreadsNonPrimeFinder.find(TestSources.smallestArray)).isTrue();
//        assertThat(TestSources.parallelThreadsNonPrimeFinder.find(TestSources.smallArray)).isFalse();
//        assertThat(TestSources.parallelThreadsNonPrimeFinder.find(TestSources.largeArray)).isFalse();
    }

}
