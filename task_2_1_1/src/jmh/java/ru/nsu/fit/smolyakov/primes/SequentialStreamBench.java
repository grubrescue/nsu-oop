package ru.nsu.fit.smolyakov.primes;


import org.openjdk.jmh.annotations.*;

@BenchmarkMode({Mode.SingleShotTime})
@Warmup(iterations = 2)
@Measurement(iterations = 2)
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
