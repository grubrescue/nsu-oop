package ru.nsu.fit.smolyakov.primes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilTest {
    @Test
    void isPrimeTest() {
        assertThat(Util.isPrime(1)).isFalse();
        assertThat(Util.isPrime(2)).isTrue();
        assertThat(Util.isPrime(3)).isTrue();
        assertThat(Util.isPrime(4)).isFalse();
        assertThat(Util.isPrime(8)).isFalse();
        assertThat(Util.isPrime(31)).isTrue();
        assertThat(Util.isPrime(62)).isFalse();
    }
}
