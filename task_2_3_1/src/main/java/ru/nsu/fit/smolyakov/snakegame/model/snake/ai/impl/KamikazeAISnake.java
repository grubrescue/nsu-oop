package ru.nsu.fit.smolyakov.snakegame.model.snake.ai.impl;

import ru.nsu.fit.smolyakov.snakegame.model.GameModel;
import ru.nsu.fit.smolyakov.snakegame.model.snake.ai.AISnake;

/**
 * Follows the player snake's head. Doesn't care about anything else, including
 * other snakes, apples, walls.
 *
 * <p>Player snake probably has some debts.
 */
@SuppressWarnings("unused")
public class KamikazeAISnake extends AISnake {
    /**
     * {@inheritDoc}
     */
    public KamikazeAISnake(GameModel gameModel) {
        super(gameModel);
    }

    /**
     * Follows the player snake's head.
     */
    @Override
    public void thinkAboutTurn() {
        var target = getGameField().getPlayerSnake().getSnakeBody().getHead();

        var xDiff = target.x() - getSnakeBody().getHead().x();
        var yDiff = target.y() - getSnakeBody().getHead().y();

        // TODO кринж убрать
        if (xDiff > 0 && getMovingDirection() != MovingDirection.LEFT) {
            setMovingDirection(MovingDirection.RIGHT);
        } else if (xDiff < 0 && !getMovingDirection().move().equals(MovingDirection.LEFT.opposite())) {
            setMovingDirection(MovingDirection.LEFT);
        } else if (yDiff > 0 && !getMovingDirection().move().equals(MovingDirection.DOWN.opposite())) {
            setMovingDirection(MovingDirection.DOWN);
        } else if (yDiff < 0 && !getMovingDirection().move().equals(MovingDirection.UP.opposite())) {
            setMovingDirection(MovingDirection.UP);
        } else if (xDiff < 0 && !getMovingDirection().move().equals(MovingDirection.RIGHT.opposite())) {
            setMovingDirection(MovingDirection.RIGHT);
        } else if (xDiff > 0 && getMovingDirection() != MovingDirection.RIGHT) {
            setMovingDirection(MovingDirection.LEFT);
        } else if (yDiff < 0 && getMovingDirection() != MovingDirection.UP) {
            setMovingDirection(MovingDirection.DOWN);
        } else if (yDiff > 0 && getMovingDirection() != MovingDirection.DOWN) {
            setMovingDirection(MovingDirection.UP);
        }
    }
}
