package ru.nsu.fit.smolyakov.snakegame.model.snake.ai;

import ru.nsu.fit.smolyakov.snakegame.model.GameField;

import java.security.SecureRandom;
import java.util.Random;

/**
 * An AI-driven snake who has a desire for a chaos.
 * He only does random movements.
 */
public class TotallyRandomAISnake extends AISnake {
    /**
     * {@inheritDoc}
     */
    public TotallyRandomAISnake(GameField gameField) {
        super(gameField);
    }

    private final Random rand = new SecureRandom();

    /**
     * Does random choice.
     */
    @Override
    public void thinkAboutTurn() {
        setMovingDirection(MovingDirection.values()[rand.nextInt(MovingDirection.values().length)]);
    }
}
