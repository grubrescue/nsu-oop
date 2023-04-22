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
 * Though, if he meets an apple on his way, he will eat it.
 */
public class StayinAliveAISnake extends AISnake {
    private final Random rand = new SecureRandom();

    /**
     * {@inheritDoc}
     */
    public StayinAliveAISnake(GameModel gameModel) {
        super(gameModel);
    }

    private boolean isCollidingTurn(MovingDirection direction) {
        var newHead = getNewHeadLocation(direction);
        return getGameField().getBarrier().met(newHead)
            || getSnakeBody().tailCollision(newHead)
            || getGameField().getAISnakeList()
                .stream()
                .filter(snake -> snake != this)
                .anyMatch(snake -> snake.getSnakeBody().headCollision(newHead))
            || getGameField().getPlayerSnake().getSnakeBody().headCollision(newHead);
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

        if (options.isEmpty()) {
            return; // уже ничего не сделаешь.............. страшно!!!
        }

        options.stream()
            .filter(movePoint -> getGameField().getApplesSet().contains(getNewHeadLocation(movePoint)))
            .findAny()
            .ifPresentOrElse(
                this::setMovingDirection,
                () -> setMovingDirection(options.get(rand.nextInt(options.size())))
            );
    }
}
