package ru.nsu.fit.smolyakov.snakegame.model.snake.ai.impl;

import ru.nsu.fit.smolyakov.snakegame.model.GameModel;
import ru.nsu.fit.smolyakov.snakegame.model.snake.SnakeBody;
import ru.nsu.fit.smolyakov.snakegame.model.snake.ai.AISnake;

/**
 * Follows the player snake's head. Doesn't care about anything else, including
 * other snakes, apples, walls.
 *
 * <p>Player snake probably has some debts.
 */
@SuppressWarnings("unused")
public class KamikazeAISnake extends StayinAliveAISnake {
    /**
     * {@inheritDoc}
     */
    public KamikazeAISnake(GameModel gameModel) {
        super(gameModel);
    }

    /**
     * Checks if the snake with specified {@code snakeBody} will collide with
     * the barrier or its own body if it turns in the given direction.
     *
     * @param snakeBody snake to check
     * @param direction direction to check
     * @return {@code true} if the snake won't collide, {@code false} otherwise
     */
    protected boolean isNonCollidingTurn(SnakeBody snakeBody, MovingDirection direction) {
        var width = getGameField().getProperties().width();
        var height = getGameField().getProperties().height();

        var newHead = getNewHeadLocation(snakeBody, direction);
        return !getGameField().getBarrier().met(newHead)
            && !snakeBody.tailCollision(newHead);
    }

    /**
     * Follows the player snake's head.
     */
    @Override
    public void thinkAboutTurn() {
        var target = getGameField().getPlayerSnake().getSnakeBody().getHead();

        var width = getGameField().getProperties().width();
        var height = getGameField().getProperties().height();
        var vec = getSnakeBody().getHead().shortestVector(target, width, height);

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
