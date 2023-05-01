package ru.nsu.fit.smolyakov.snakegame.unittests;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.snakegame.utils.Point;

import static org.assertj.core.api.Assertions.*;

public class PointTest {
    @Test
    void randomTest() {
        assertThat(Point.random(1, 1)).isEqualTo(Point.ZERO);
    }

    @Test
    void shiftTest() {
        assertThat(Point.ZERO.shift(Point.ZERO)).isEqualTo(Point.ZERO);
        assertThat(Point.ZERO.shift(new Point(1, 1))).isEqualTo(new Point(1, 1));
    }

    @Test
    void shiftWithLimitsTest() {
        assertThat(Point.ZERO.shift(new Point(1, 1), 2, 2)).isEqualTo(new Point(1, 1));
        assertThat(Point.ZERO.shift(new Point(2, 2), 2, 2)).isEqualTo(Point.ZERO);

        assertThat(Point.ZERO.shift(new Point(-3, -3), 2, 2)).isEqualTo(new Point(1, 1));
        assertThat(Point.ZERO.shift(new Point(-4, -4), 2, 2)).isEqualTo(Point.ZERO);
    }

    @Test
    void shiftWithLimitsExceptionTest() {
        assertThatThrownBy(() -> Point.ZERO.shift(new Point(1, 1), 0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Limits must be positive");
    }

    @Test
    void connectedTest() {
        assertThat(Point.ZERO.connected(Point.ZERO)).isTrue();
        assertThat(Point.ZERO.connected(new Point(1, 1))).isFalse();
        assertThat(Point.ZERO.connected(new Point(1, 0))).isTrue();
        assertThat(Point.ZERO.connected(new Point(0, 1))).isTrue();
    }

    @Test
    void distanceTest() {
        assertThat(Point.ZERO.distance(Point.ZERO)).isEqualTo(0);
        assertThat(Point.ZERO.distance(new Point(3, 4))).isEqualTo(5);

        assertThat(new Point(1, 1).distance(new Point(4, 5))).isEqualTo(5);
    }

    @Test
    void gettersTest() {
        assertThat(Point.ZERO.x()).isEqualTo(0);
        assertThat(Point.ZERO.y()).isEqualTo(0);
    }
}
