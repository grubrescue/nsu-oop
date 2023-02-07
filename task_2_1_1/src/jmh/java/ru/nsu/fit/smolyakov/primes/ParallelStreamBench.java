package ru.nsu.fit.smolyakov.primes;


import org.openjdk.jmh.annotations.*;

@BenchmarkMode({Mode.Throughput})
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class ParallelStreamBench {
    @Benchmark
    public void smallerTest() {
        var finder = new ParallelStreamNonPrimeFinder();
        finder.containsNonPrime(JmhSources.smallestArrayTrue);
    }

    @Benchmark
    public void smallTest() {
        var finder = new ParallelStreamNonPrimeFinder();
        finder.containsNonPrime(JmhSources.smallArrayFalse);
    }

    @Benchmark
    public void largeTest() {
        var finder = new ParallelStreamNonPrimeFinder();
        finder.containsNonPrime(JmhSources.largeArrayTrue);
    }
}
