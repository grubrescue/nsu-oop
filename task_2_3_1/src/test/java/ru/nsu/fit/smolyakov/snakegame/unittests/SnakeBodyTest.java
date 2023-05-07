package ru.nsu.fit.smolyakov.snakegame.unittests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.snakegame.model.snake.SnakeBody;
import ru.nsu.fit.smolyakov.snakegame.utils.Point;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SnakeBodyTest {
    SnakeBody snakeBody;

    @BeforeEach
    void init() {
        snakeBody = new SnakeBody(new Point(5, 5), List.of(new Point(5, 6), new Point(5, 7)));
    }

    @Test
    void emptyTailConstructorTest() {
        assertThatThrownBy(() -> new SnakeBody(new Point(5, 5), List.of()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullConstructorTest() {
        assertThatThrownBy(() -> new SnakeBody(null, List.of(new Point(5, 6))))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new SnakeBody(new Point(5, 5), null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void moveWithoutGrowTest() {
        snakeBody.move(new Point(5, 4), false);
        assertThat(snakeBody).isEqualTo(
            new SnakeBody(new Point(5, 4), List.of(new Point(5, 5), new Point(5, 6)))
        );
    }

    @Test
    void moveWithGrowTest() {
        snakeBody.move(new Point(5, 4), true);
        assertThat(snakeBody).isEqualTo(
            new SnakeBody(new Point(5, 4), List.of(new Point(5, 5), new Point(5, 6), new Point(5, 7)))
        );
    }

    @Test
    void deathCollisionTest() {
        assertThat(snakeBody.deathCollision(new Point(5, 5))).isTrue();
        assertThat(snakeBody.deathCollision(new Point(5, 6))).isTrue();
        assertThat(snakeBody.deathCollision(new Point(5, 7))).isFalse();
    }

    @Test
    void headCollisionTest() {
        assertThat(snakeBody.headCollision(new Point(5, 5))).isTrue();
        assertThat(snakeBody.headCollision(new Point(5, 6))).isFalse();
        assertThat(snakeBody.headCollision(new Point(5, 7))).isFalse();
    }

    @Test
    void tailCollisionTest() {
        assertThat(snakeBody.tailCollision(new Point(5, 5))).isFalse();
        assertThat(snakeBody.tailCollision(new Point(5, 6))).isTrue();
        assertThat(snakeBody.tailCollision(new Point(5, 7))).isTrue();
    }

    @Test
    void cutTailTest() {
        assertThat(snakeBody.cutTail(new Point(5, 7))).isTrue();
        assertThat(snakeBody).isEqualTo(
            new SnakeBody(new Point(5, 5), List.of(new Point(5, 6)))
        );

        assertThat(snakeBody.cutTail(new Point(5, 8))).isFalse();
        assertThat(snakeBody).isEqualTo(
            new SnakeBody(new Point(5, 5), List.of(new Point(5, 6)))
        );
    }

    //
    @Test
    void hashcodeAndEqualsTest() {
        var equalSnake
            = new SnakeBody(new Point(5, 5), List.of(new Point(5, 6), new Point(5, 7)));
        var nonEqualSnake = new SnakeBody(new Point(4, 5), List.of(new Point(4, 6)));

        assertThat(snakeBody.hashCode()).isEqualTo(equalSnake.hashCode());

        assertThat(snakeBody).isEqualTo(equalSnake);
        assertThat(snakeBody).isEqualTo(snakeBody);

        assertThat(snakeBody).isNotEqualTo(nonEqualSnake);
        assertThat(snakeBody).isNotEqualTo(null);
        assertThat(snakeBody).isNotEqualTo(new Object());
    }

    @Test
    void copyTest() {
        var copy = snakeBody.copy();

        assertThat(copy).isEqualTo(snakeBody);
        assertThat(copy).isNotSameAs(snakeBody);
    }
}
