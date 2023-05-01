package ru.nsu.fit.smolyakov.snakegame.integrationtests;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.snakegame.ExampleProperties;
import ru.nsu.fit.smolyakov.snakegame.model.GameModel;
import ru.nsu.fit.smolyakov.snakegame.model.GameModelImpl;
import ru.nsu.fit.smolyakov.snakegame.presenter.SnakePresenter;
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
    GameProperties properties = ExampleProperties.properties;

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

    private Point head() {
        return model.getPlayerSnake().getSnakeBody().getHead();
    }

    @Test
    public void test() {
        assertThat(presenter.frameUpdaterRunning).isFalse();
        presenter.start();

        assertThat(presenter.frameUpdaterRunning).isTrue();
        assertThat(presenter.scoreAmount).isEqualTo(0);

        // TODO wip
//        var target = model.getApplesSet().stream().findAny().get();
//
//        var xDiff = target.point().x() - head().x();
//        var yDiff = target.point().y() - head().y();
//
//        System.out.println(head());
//        presenter.nextStep();
//        System.out.println(head());

        return;

//        if (xDiff > 0) { // змейка ползет вверх, а надо вниз
//            SnakePresenter.EventAction.RIGHT.execute(presenter);
//            presenter.nextStep();
//
//            SnakePresenter.EventAction.DOWN.execute(presenter);
//            presenter.nextStep();
//
//            xDiff = target.point().x() - head().x();
//            while (xDiff > 0) {
//                presenter.nextStep();
//                System.out.println("down" + head());
//
//                xDiff = target.point().x() - head().x();
//            }
//        } else if (xDiff < 0) {
//            do {
//                presenter.nextStep();
//                System.out.println("up" + head());
//
//                xDiff = target.point().x() - head().x();
//            } while (xDiff != 0);
//        }
//
//        while (yDiff > 0) {
//            SnakePresenter.EventAction.RIGHT.execute(presenter);
//            presenter.nextStep();
//            System.out.println("right" + head());
//
//            yDiff = target.point().y() - head().y();
//        }
//        while (yDiff < 0) {
//            SnakePresenter.EventAction.LEFT.execute(presenter);
//            presenter.nextStep();
//            System.out.println("left" + head());
//
//            yDiff = target.point().y() - head().y();
//        }
//
//        assertThat(target).isNotIn(model.getApplesSet());
//        assertThat(presenter.scoreAmount).isEqualTo(1);
    }
}
