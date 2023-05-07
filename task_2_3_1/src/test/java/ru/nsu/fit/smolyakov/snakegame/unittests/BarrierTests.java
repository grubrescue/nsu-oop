package ru.nsu.fit.smolyakov.snakegame.unittests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.snakegame.ExampleProperties;
import ru.nsu.fit.smolyakov.snakegame.model.Barrier;
import ru.nsu.fit.smolyakov.snakegame.properties.level.*;
import ru.nsu.fit.smolyakov.snakegame.utils.Point;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BarrierTests {
    @Test
    public void borderBarrierTest() {
        var barrier =
            Barrier.fromProperties(
                ExampleProperties.properties
                    .withLevel(
                        new CustomFileLevel("40x20_border.txt")
                    )
            );

        assertThat(barrier.barrierPoints())
            .isEqualTo(
                Set.of(
                    new Point(0, 0),
                    new Point(0, 1),
                    new Point(0, 2),
                    new Point(0, 3),
                    new Point(0, 4),
                    new Point(0, 5),

                    new Point(1, 0),
                    new Point(2, 0),
                    new Point(3, 0),
                    new Point(4, 0),
                    new Point(5, 0)
                )
            );
    }

    @Test
    public void emptyBarrierTest() {
        var barrier =
            Barrier.fromProperties(
                ExampleProperties.properties
                    .withLevel(
                        new EmptyLevel()
                    )
            );

        assertThat(barrier.barrierPoints())
            .isEqualTo(
                Set.of()
            );
    }

    @Test
    public void fileBarrierTest() {
        var barrier =
            Barrier.fromProperties(
                ExampleProperties.properties
                    .withLevel(
                        new BorderLevel()
                    )
            );

        assertThat(barrier.barrierPoints())
            .isEqualTo(
                Set.of(
                    new Point(0, 0),

                    new Point(0, 1),
                    new Point(0, 2),
                    new Point(0, 3),
                    new Point(0, 4),
                    new Point(0, 5),

                    new Point(1, 0),
                    new Point(2, 0),
                    new Point(3, 0),
                    new Point(4, 0),
                    new Point(5, 0),

                    new Point(1, 5),
                    new Point(2, 5),
                    new Point(3, 5),
                    new Point(4, 5),

                    new Point(5, 1),
                    new Point(5, 2),
                    new Point(5, 3),
                    new Point(5, 4),

                    new Point(5, 5)
                )
            );
    }

    @Test
    void randomBarrierTest() {
        var barrier =
            Barrier.fromProperties(
                ExampleProperties.properties
                    .withLevel(
                        new RandomLevel(0.5)
                    )
            );

        assertThat(barrier.barrierPoints().size())
            .isLessThanOrEqualTo(
                ExampleProperties.properties.height() * ExampleProperties.properties.width() / 2
            );
    }
}
