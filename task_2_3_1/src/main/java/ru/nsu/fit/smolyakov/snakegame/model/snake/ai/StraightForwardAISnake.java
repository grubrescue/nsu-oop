package ru.nsu.fit.smolyakov.snakegame.model.snake.ai;

import ru.nsu.fit.smolyakov.snakegame.model.GameField;

/**
 * An AI-driven samurai snake that always follow the original
 * movement direction. Though, {@link #thinkAboutTurn()} method
 * does nothing.
 */
public class StraightForwardAISnake extends AISnake {
    /**
     * {@inheritDoc}
     */
    public StraightForwardAISnake(GameField gameField) {
        super(gameField);
    }

    /**
     * Does nothing.
     */
    @Override
    public void thinkAboutTurn() {
        // do nothing, this one is stupid
    }
}
