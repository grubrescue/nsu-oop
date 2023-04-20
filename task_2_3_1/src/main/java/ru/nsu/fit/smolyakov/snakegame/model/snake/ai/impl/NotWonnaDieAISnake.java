package ru.nsu.fit.smolyakov.snakegame.model.snake.ai.impl;

import ru.nsu.fit.smolyakov.snakegame.model.GameModel;
import ru.nsu.fit.smolyakov.snakegame.model.snake.ai.AISnake;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * An AI-driven snake that doesn't want to die.
 * He only does random movements, trying to avoid collisions with the barrier.
 */
public class NotWonnaDieAISnake extends AISnake {
    /**
     * {@inheritDoc}
     */
    public NotWonnaDieAISnake(GameModel gameModel) {
        super(gameModel);
    }

    private final Random rand = new SecureRandom();

    private boolean isCollidingTurn(MovingDirection direction) {
        return getGameField().getBarrier().met(getSnakeBody().getHead().shift(direction.move()));
    }

    /**
     * Does random choice, trying to exclude barrier collision variants
     */
    @Override
    public void thinkAboutTurn() {
        List<MovingDirection> options = new LinkedList<>(
            Arrays.stream(MovingDirection.values())
                .filter(movePoint -> !isCollidingTurn(movePoint))
                .filter(movePoint -> !movePoint.move().equals(getMovingDirection().opposite()))
                .toList()
        );

        if (!options.isEmpty()) {
            setMovingDirection((MovingDirection) options.get(rand.nextInt(options.size())));
        }
    }
}
