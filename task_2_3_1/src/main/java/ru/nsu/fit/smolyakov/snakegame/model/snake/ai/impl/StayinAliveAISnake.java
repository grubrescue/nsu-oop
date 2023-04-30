package ru.nsu.fit.smolyakov.snakegame.model.snake.ai.impl;

import ru.nsu.fit.smolyakov.snakegame.model.Apple;
import ru.nsu.fit.smolyakov.snakegame.model.GameModel;
import ru.nsu.fit.smolyakov.snakegame.model.snake.SnakeBody;
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

    /**
     * Checks if the snake with specified {@code snakeBody} will collide with
     * the barrier or its own body if it turns in the given direction.
     *
     * @param direction direction to check
     * @return true if the snake will collide, false otherwise
     */
    protected boolean isNonCollidingTurn(SnakeBody snakeBody, MovingDirection direction) {
        var newHead = getNewHeadLocation(snakeBody, direction);
        return !getGameField().getBarrier().met(newHead)
            && !snakeBody.tailCollision(newHead)
            && getGameField().getAISnakeList()
            .stream()
            .filter(snake -> snake.getSnakeBody() != snakeBody)
            .noneMatch(snake -> snake.getSnakeBody().headCollision(newHead))
            && !getGameField().getPlayerSnake().getSnakeBody().headCollision(newHead);
    }

    /**
     * Checks if the snake will collide with the barrier or its own body
     * if it turns in the given direction.
     *
     * @param direction direction to check
     * @return true if the snake will collide, false otherwise
     */
    protected boolean isNonCollidingTurn(MovingDirection direction) {
        return isNonCollidingTurn(getSnakeBody(), direction);
    }

    /**
     * Does random choice, trying to exclude barrier collision variants.
     */
    @Override
    public void thinkAboutTurn() {
        List<MovingDirection> options = new LinkedList<>(
            Arrays.stream(MovingDirection.values())
                .filter(this::isNonCollidingTurn)
                .filter(movePoint -> !movePoint.move().equals(getMovingDirection().opposite()))
                .toList()
        );

        if (options.isEmpty()) {
            return; // уже ничего не сделаешь.............. страшно!!!
        }

        options.stream()
            .filter(movePoint -> getGameField().getApplesSet().contains(new Apple(getNewHeadLocation(movePoint))))
            .findAny()
            .ifPresentOrElse(
                this::setMovingDirection,
                () -> setMovingDirection(options.get(rand.nextInt(options.size())))
            );
    }
}
