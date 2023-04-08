package ru.nsu.fit.smolyakov.snakegame.unittests.leveldto;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.snakegame.properties.level.RandomLevel;

import static org.assertj.core.api.Assertions.assertThat;
public class RandomLevelTest {
    @Test
    void test() {
        var level = new RandomLevel(0.5);

        assertThat(level.getDensity()).isEqualTo(0.5);
    }
}
