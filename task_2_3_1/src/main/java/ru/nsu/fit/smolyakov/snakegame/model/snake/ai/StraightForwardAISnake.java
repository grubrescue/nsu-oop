package ru.nsu.fit.smolyakov.snakegame.model.snake.ai;

import ru.nsu.fit.smolyakov.snakegame.model.GameField;

public class StraightForwardAISnake extends AISnake {
    public StraightForwardAISnake(GameField gameField) {
        super(gameField);
    }

    @Override
    public void thinkAboutTurn() {
        // do nothing, this one is stupid
    }
}
