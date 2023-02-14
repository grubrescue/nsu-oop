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
//
    @Test
    void parallelStreamNonPrimeFinderTest() {
        assertThat(TestSources.parallelStreamNonPrimeFinder.find(TestSources.smallestArray)).isTrue();
        assertThat(TestSources.parallelStreamNonPrimeFinder.find(TestSources.smallArray)).isFalse();
        assertThat(TestSources.parallelStreamNonPrimeFinder.find(TestSources.largeArray)).isFalse();
    }

    @Test
    void sequentialStreamNonPrimeFinderBench() {
        assertThat(TestSources.sequentialStreamNonPrimeFinder.find(TestSources.smallestArray)).isTrue();
        assertThat(TestSources.sequentialStreamNonPrimeFinder.find(TestSources.smallArray)).isFalse();
        assertThat(TestSources.sequentialStreamNonPrimeFinder.find(TestSources.largeArray)).isFalse();
    }

    @Test
    void parallelStreamNonPrimeFinderBench() {
        assertThat(TestSources.parallelStreamNonPrimeFinder.find(TestSources.smallestArray)).isTrue();
        assertThat(TestSources.parallelStreamNonPrimeFinder.find(TestSources.smallArray)).isFalse();
        assertThat(TestSources.parallelStreamNonPrimeFinder.find(TestSources.largeArray)).isFalse();
    }
}
