package ru.nsu.fit.smolyakov.primes;


import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;

import static org.assertj.core.api.Assertions.assertThat;

@BenchmarkMode(Mode.All)
@Warmup(iterations = 10) // число итераций для прогрева нашей функции
@Measurement(iterations = 10, batchSize = 10)
public class PrimeFinderBench {
    @Benchmark
    void sequentialStreamNonPrimeFinderBench() {
        assertThat(TestSources.sequentialStreamNonPrimeFinder.find(TestSources.smallestArray)).isTrue();
        assertThat(TestSources.sequentialStreamNonPrimeFinder.find(TestSources.smallArray)).isFalse();
//        assertThat(TestSources.sequentialStreamNonPrimeFinder.find(TestSources.largeArray)).isFalse();
    }

    @Benchmark
    void parallelStreamNonPrimeFinderBench() {
        assertThat(TestSources.parallelStreamNonPrimeFinder.find(TestSources.smallestArray)).isTrue();
        assertThat(TestSources.parallelStreamNonPrimeFinder.find(TestSources.smallArray)).isFalse();
//        assertThat(TestSources.parallelStreamNonPrimeFinder.find(TestSources.largeArray)).isFalse();
    }
}
