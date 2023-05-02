package ru.nsu.fit.smolyakov.snakegame.unittests;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.snakegame.model.snake.CollisionSolver;
import ru.nsu.fit.smolyakov.snakegame.model.snake.SnakeBody;
import ru.nsu.fit.smolyakov.snakegame.utils.Point;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class CollisionSolverTest {
    @Test
    void bothDeadTest() {
        assertThat(
            CollisionSolver.solve(
                new SnakeBody(
                    new Point(1, 1),
                    List.of(
                        new Point(1, 2),
                        new Point(1, 3)
                    )
                ),
                new SnakeBody(
                    new Point(1, 1),
                    List.of(
                        new Point(2, 1),
                        new Point(3, 1)
                    )
                )
            )
        ).isEqualTo(CollisionSolver.Result.BOTH_DEAD);

        assertThat(
            CollisionSolver.solve(
                new SnakeBody(
                    new Point(1, 1),
                    List.of(
                        new Point(1, 2),
                        new Point(1, 3)
                    )
                ),
                new SnakeBody(
                    new Point(0, 1),
                    List.of(
                        new Point(1, 1),
                        new Point(2, 1)
                    )
                )
            )
        ).isEqualTo(CollisionSolver.Result.BOTH_DEAD);
    }

    @Test
    void bothAliveTest() {
        assertThat(
            CollisionSolver.solve(
                new SnakeBody(
                    new Point(1, 1),
                    List.of(
                        new Point(1, 2),
                        new Point(1, 3)
                    )
                ),
                new SnakeBody(
                    new Point(1, 3),
                    List.of(
                        new Point(2, 3),
                        new Point(3, 3)
                    )
                )
            )
        ).isEqualTo(CollisionSolver.Result.BOTH_ALIVE);

        assertThat(
            CollisionSolver.solve(
                new SnakeBody(
                    new Point(2, 1),
                    List.of(
                        new Point(2, 2),
                        new Point(2, 3)
                    )
                ),
                new SnakeBody(
                    new Point(0, 1),
                    List.of(
                        new Point(1, 1),
                        new Point(2, 1)
                    )
                )
            )
        ).isEqualTo(CollisionSolver.Result.BOTH_ALIVE);
    }
}
