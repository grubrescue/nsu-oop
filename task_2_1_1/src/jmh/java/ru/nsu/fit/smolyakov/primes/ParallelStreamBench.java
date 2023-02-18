package ru.nsu.fit.smolyakov.primes;


import org.openjdk.jmh.annotations.*;

@BenchmarkMode({Mode.SingleShotTime})
@Warmup(iterations = 2)
@Measurement(iterations = 2)
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
