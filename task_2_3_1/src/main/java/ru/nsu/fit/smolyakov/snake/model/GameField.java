package ru.nsu.fit.smolyakov.snake.model;

import ru.nsu.fit.smolyakov.snake.model.Snake;

// TODO добавить препятствия ????
// TODO по хорошему вообще ченить написать...
public record GameField(int height, int width) {
    public boolean metBarrier(Snake snake) {
        return false; // TODO
    }
}
