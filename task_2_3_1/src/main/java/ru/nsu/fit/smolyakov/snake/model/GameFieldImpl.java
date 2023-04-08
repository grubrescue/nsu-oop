package ru.nsu.fit.smolyakov.snake.model;

import java.util.List;
import java.util.Optional;

public class GameFieldImpl implements GameField {
    private int height;
    private int width;

    private Barrier barrier;
    private Apple apple;

    private Snake playerSnake;

    @Override
    public List<Snake> getAISnakes() {
        return null;
    }

    @Override
    public Snake getPlayerSnake() {
        return playerSnake;
    }

    @Override
    public Optional<Apple> getApple() {
        return Optional.empty();
    }


}
