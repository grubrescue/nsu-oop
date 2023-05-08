package ru.nsu.fit.smolyakov.snakegame.model.snake.ai.impl;

import ru.nsu.fit.smolyakov.snakegame.model.Apple;
import ru.nsu.fit.smolyakov.snakegame.model.GameModel;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

/**
 * This one hunts for an apple, and tries not to die at the same time.
 * Though, he doesn't look ahead more than for a one step, so could easily put
 * himself to a problematic situation.
 */
@SuppressWarnings("unused")
public class GreedyAISnake extends StayinAliveAISnake {
    private final Random rand = new SecureRandom();
    private Apple target = null;

    /**
     * {@inheritDoc}
     */
    public GreedyAISnake(GameModel gameModel) {
        super(gameModel);
    }

    /**
     * Finds the closest apple to the snake's head.
     *
     * @return {@link Optional} of the closest apple,
     * or {@link Optional#empty()} if there are no apples on the field
     */
    protected Optional<Apple> findNewTarget() {
        var width = getGameField().getProperties().width();
        var height = getGameField().getProperties().height();

        return getGameField()
            .getApplesSet()
            .stream()
            .max((Apple a, Apple b) -> {
                var aDist = getSnakeBody().getHead().cathetusDistance(a.point(), width, height);
                var bDist = getSnakeBody().getHead().cathetusDistance(b.point(), width, height);
                return Integer.compare(aDist, bDist);
            });
    }

    /**
     * Does such choices that the snake will move towards the closest apple
     * and will not collide with the barrier, other snakes or its own body.
     */
    @Override
    public void thinkAboutTurn() {
        if (target == null || !getGameField().getApplesSet().contains(target)) {
            target = findNewTarget().orElse(null);
        }

        if (target == null) {
            return;
        }

        var width = getGameField().getProperties().width();
        var height = getGameField().getProperties().height();
        var vec = getSnakeBody().getHead().shortestVector(target.point(), width, height);

        if (vec.x() > 0 && isNonCollidingTurn(MovingDirection.RIGHT)) {
            setMovingDirection(MovingDirection.RIGHT);
        } else if (vec.x() < 0 && isNonCollidingTurn(MovingDirection.LEFT)) {
            setMovingDirection(MovingDirection.LEFT);
        } else if (vec.y() > 0 && isNonCollidingTurn(MovingDirection.DOWN)) {
            setMovingDirection(MovingDirection.DOWN);
        } else if (vec.y() < 0 && isNonCollidingTurn(MovingDirection.UP)) {
            setMovingDirection(MovingDirection.UP);
        } else {
            super.thinkAboutTurn();
        }
    }
}
