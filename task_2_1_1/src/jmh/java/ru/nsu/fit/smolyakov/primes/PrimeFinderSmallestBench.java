package ru.nsu.fit.smolyakov.primes;


import org.openjdk.jmh.annotations.*;

import static org.assertj.core.api.Assertions.assertThat;

@BenchmarkMode({Mode.SingleShotTime})
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@State(Scope.Benchmark)
public class PrimeFinderSmallestBench {
    private static int[] source = TestSources.smallArrayFalse;
    @Benchmark
    public void sequentialStreamNonPrimeFinderBench() {
        JmhSources.sequentialStreamNonPrimeFinder.containsNonPrime(source);

    }

    @Benchmark
    public void parallelStreamNonPrimeFinderBench() {
        JmhSources.parallelStreamNonPrimeFinder.containsNonPrime(source);
    }

//    @Param({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"})
//    private int amountOfCores;
//
//    @Benchmark
//    public void parallelThreadsNonPrimeFinderBench() {
//        JmhSources.parallelThreadsNonPrimeFindersList.get(amountOfCores).containsNonPrime(source);
//    }
}
