package ru.nsu.fit.smolyakov.primes;


import org.openjdk.jmh.annotations.*;

@BenchmarkMode({Mode.Throughput})
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class SequentialStreamBench {
    @Benchmark
    public void smallerTest() {
        var finder = new SequentialStreamNonPrimeFinder();
        finder.containsNonPrime(JmhSources.smallestArrayTrue);
    }

    @Benchmark
    public void smallTest() {
        var finder = new SequentialStreamNonPrimeFinder();
        finder.containsNonPrime(JmhSources.smallArrayFalse);
    }

    @Benchmark
    public void largeTest() {
        var finder = new SequentialStreamNonPrimeFinder();
        finder.containsNonPrime(JmhSources.largeArrayTrue);
    }
}
