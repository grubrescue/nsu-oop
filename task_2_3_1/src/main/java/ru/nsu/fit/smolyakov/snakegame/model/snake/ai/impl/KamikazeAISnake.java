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

        var width = getGameField().getProperties().width();
        var height = getGameField().getProperties().height();
        var vec = getSnakeBody().getHead().shortestVector(target, width, height);

        System.out.println("the shortest path to the "+ target + " is " + vec);

        if (vec.x() > 0 && !getMovingDirection().move().equals(MovingDirection.RIGHT.opposite())) {
            setMovingDirection(MovingDirection.RIGHT);
        } else if (vec.x() < 0 && !getMovingDirection().move().equals(MovingDirection.LEFT.opposite())) {
            setMovingDirection(MovingDirection.LEFT);
        } else if (vec.y() > 0 && !getMovingDirection().move().equals(MovingDirection.DOWN.opposite())) {
            setMovingDirection(MovingDirection.DOWN);
        } else if (vec.y() < 0 && !getMovingDirection().move().equals(MovingDirection.UP.opposite())) {
            setMovingDirection(MovingDirection.UP);
        }
    }
}
