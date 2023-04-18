package ru.nsu.fit.smolyakov.snakegame.model.snake.ai;

import ru.nsu.fit.smolyakov.snakegame.model.GameField;
import ru.nsu.fit.smolyakov.snakegame.model.snake.Snake;

/**
 * An abstract class for AI-driven snakes.
 * Implementing classes should override {@link #thinkAboutTurn()} method,
 * that is called every time the snake is about to make a turn.
 * This method is supposed to contain logic that decides which direction the snake should
 * turn to.
 */
public abstract class AISnake extends Snake {
    /**
     * Instantiates an AI-snake in a random position on a field.
     *
     * @param gameField game field
     */
    public AISnake(GameField gameField) {
        super(gameField);
    }

    public abstract void thinkAboutTurn();
}
