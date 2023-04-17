package ru.nsu.fit.smolyakov.snakegame.model.snake.ai;

import ru.nsu.fit.smolyakov.snakegame.model.GameField;
import ru.nsu.fit.smolyakov.snakegame.model.snake.Snake;

public abstract class AISnake extends Snake {
    /**
     * Instantiates a snake in a random position on a field.
     *
     * @param gameField
     */
    public AISnake(GameField gameField) {
        super(gameField);
    }

    public abstract void thinkAboutTurn();
}
