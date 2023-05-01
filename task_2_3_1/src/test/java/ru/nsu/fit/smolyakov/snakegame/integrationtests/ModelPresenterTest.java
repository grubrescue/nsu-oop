package ru.nsu.fit.smolyakov.snakegame.integrationtests;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.snakegame.model.GameModel;
import ru.nsu.fit.smolyakov.snakegame.model.GameModelImpl;
import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;
import ru.nsu.fit.smolyakov.snakegame.properties.GameSpeed;
import ru.nsu.fit.smolyakov.snakegame.utils.Point;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Properties;
import java.util.Set;

public class ModelPresenterTest {
    TestingSnakePresenter presenter;
    GameModel model;
    GameProperties properties = new GameProperties(
        6,
        6,
        1,
        1,
        "40x20_border.txt",
        List.of(),
        GameSpeed.SPEED_0
    );

    @BeforeEach
    void init() {
        presenter = new TestingSnakePresenter();
        model = new GameModelImpl(properties);
        presenter.setProperties(properties);
        presenter.setModel(model);

    }

    @Test
    public void barrierInitializationTest() {
        assertThat(model.getBarrier().barrierPoints())
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
    public void snakeInitializationTest() {
        var body = model.getPlayerSnake().getSnakeBody();
        assertThat(body.)
            .isEqualTo(
                Set.of(
                    new Point(1, 1),
                    new Point(1, 2),
                    new Point(1, 3),
                    new Point(1, 4),
                    new Point(1, 5)
                )
            );
    }


    @Test
    public void test() {
        Assertions.assertThat(presenter.frameUpdaterRunning).isFalse();
        presenter.start();

        Assertions.assertThat(presenter.frameUpdaterRunning).isTrue();
        Assertions.assertThat(presenter.scoreAmount).isEqualTo(0);
    }
}
