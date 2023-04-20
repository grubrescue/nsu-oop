package ru.nsu.fit.smolyakov.snakegame.model.snake.ai.impl;

import ru.nsu.fit.smolyakov.snakegame.model.GameModel;
import ru.nsu.fit.smolyakov.snakegame.model.snake.ai.AISnake;

/**
 * An AI-driven samurai snake that always follow the original
 * movement direction. Though, {@link #thinkAboutTurn()} method
 * does nothing.
 */
public class StraightForwardAISnake extends AISnake {
    /**
     * {@inheritDoc}
     */
    public StraightForwardAISnake(GameModel gameModel) {
        super(gameModel);
    }

    /**
     * Does nothing.
     */
    @Override
    public void thinkAboutTurn() {
        // do nothing, this one is stupid
    }
}
