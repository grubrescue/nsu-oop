package ru.nsu.fit.smolyakov.snakegame.model.snake.ai.impl;

import ru.nsu.fit.smolyakov.snakegame.model.Apple;
import ru.nsu.fit.smolyakov.snakegame.model.GameModel;
import ru.nsu.fit.smolyakov.snakegame.model.snake.ai.AISnake;

import java.security.SecureRandom;
import java.util.Random;

// TODO TODO TDOD TODO TODO

/**
 * а её еще сделать надо, че доки то впустую писать...
 */
public class WannaEatAISnake extends AISnake {
    /**
     * {@inheritDoc}
     */
    public WannaEatAISnake(GameModel gameModel) {
        super(gameModel);
    }

    private final Random rand = new SecureRandom();
    private Apple target = null;

    private void findNewTarget() {
        target = getGameField().getApplesSet().stream().skip(rand.nextLong(getGameField().getApplesSet().size() - 1)).findFirst().orElse(null);
    }

    @Override
    public void thinkAboutTurn() {
        if (target == null || !getGameField().getApplesSet().contains(target)) {
            findNewTarget();
        }

        // TODO todo
    }
}
