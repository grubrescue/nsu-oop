package ru.nsu.fit.smolyakov.snakegame.unittests;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.snakegame.utils.Point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void shortestVectorTest() {
        assertThat(new Point(3, 3)
            .shortestVector(new Point(5, 5), 100, 100)
        ).isEqualTo(new Point(2, 2));

        assertThat(new Point(3, 3)
            .shortestVector(new Point(1, 1), 100, 100)
        ).isEqualTo(new Point(-2, -2));

        assertThat(new Point(1, 1)
            .shortestVector(new Point(4, 4), 5, 5)
        ).isEqualTo(new Point(-2, -2));

        assertThat(new Point(1, 4)
            .shortestVector(new Point(4, 4), 5, 5)
        ).isEqualTo(new Point(-2, 0));

        assertThat(new Point(4, 4)
            .shortestVector(new Point(0, 0), 5, 5)
        ).isEqualTo(new Point(1, 1));
    }


//    @Test
//    void cathetusDistanceTest() {
//        assertThat(Point.ZERO.cathetusDistance(new Point(10, 0), 11, 11))
//            .isEqualTo()
//    }

    @Test
    void gettersTest() {
        assertThat(Point.ZERO.x()).isEqualTo(0);
        assertThat(Point.ZERO.y()).isEqualTo(0);
    }
}
