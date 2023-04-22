package ru.nsu.fit.smolyakov.snakegame.model.snake.ai.impl;

import ru.nsu.fit.smolyakov.snakegame.model.GameModel;
import ru.nsu.fit.smolyakov.snakegame.model.snake.ai.AISnake;

/**
 * An AI-driven samurai snake that always follow the original
 * movement direction. One has no goal, only a path.
 * Obviously, {@link #thinkAboutTurn()} method
 * does nothing.
 */
public class SamuraiAISnake extends AISnake {
    /**
     * {@inheritDoc}
     */
    public SamuraiAISnake(GameModel gameModel) {
        super(gameModel);
    }

    /**
     * Does nothing.
     */
    @Override
    public void thinkAboutTurn() {
        // do nothing
    }
}
