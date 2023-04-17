package ru.nsu.fit.smolyakov.snakegame.model.snake.ai;

import ru.nsu.fit.smolyakov.snakegame.model.GameField;

import java.security.SecureRandom;
import java.util.Random;

public class TotallyRandomAISnake extends AISnake {
    public TotallyRandomAISnake(GameField gameField) {
        super(gameField);
    }

    private final Random rand = new SecureRandom();

    @Override
    public void thinkAboutTurn() {
        setMovingDirection(MovingDirection.values()[rand.nextInt(MovingDirection.values().length)]);
    }
}
