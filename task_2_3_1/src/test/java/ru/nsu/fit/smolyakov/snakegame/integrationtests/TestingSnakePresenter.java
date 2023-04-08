package ru.nsu.fit.smolyakov.snakegame.integrationtests;

import ru.nsu.fit.smolyakov.snakegame.model.Apple;
import ru.nsu.fit.smolyakov.snakegame.model.Barrier;
import ru.nsu.fit.smolyakov.snakegame.model.snake.Snake;
import ru.nsu.fit.smolyakov.snakegame.presenter.SnakePresenter;

public class TestingSnakePresenter extends SnakePresenter {
    boolean frameUpdaterRunning = false;
    int scoreAmount = 0;

    public void nextStep() {
        update();
    }

    @Override
    protected void runFramesUpdater() {
        frameUpdaterRunning = true;
    }

    @Override
    protected void stopFramesUpdater() {
        frameUpdaterRunning = false;
    }

    @Override
    protected void setScoreAmount(int scoreAmount) {
        this.scoreAmount = scoreAmount;
    }

    @Override
    protected void drawApple(Apple apple) {

    }

    @Override
    protected void drawBarrier(Barrier barrier) {

    }

    @Override
    protected void drawPlayerSnake(Snake snake) {

    }

    @Override
    protected void drawEnemySnake(Snake snake) {

    }

    @Override
    protected void showMessage(String message) {

    }

    @Override
    protected void clear() {

    }

    @Override
    protected void close() {

    }

    @Override
    protected void refresh() {

    }
}
