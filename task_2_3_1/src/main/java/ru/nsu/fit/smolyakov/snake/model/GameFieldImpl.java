package ru.nsu.fit.smolyakov.snake.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GameFieldImpl implements GameField {
    private int width;
    private int height;
    private int maxApples;

    private Snake playerSnake;
    private List<Snake> AISnakesList;
    private Set<Apple> applesSet;
    private Barrier barrier;

    @Override
    public List<Snake> getAISnakeList() {
        return AISnakesList;
    }

    @Override
    public Snake getPlayerSnake() {
        return playerSnake;
    }

    @Override
    public Set<Apple> getApplesSet() {
        return applesSet;
    }

    @Override
    public Barrier getBarrier() {
        return barrier;
    }

    @Override
    public boolean isFree(Point point) {
        return playerSnake.getSnakeBody().headCollision(point) ||
            playerSnake.getSnakeBody().tailCollision(point) ||
            barrier.met(point) ||
            applesSet.stream().anyMatch(apple -> apple.location().equals(point)) ||
            AISnakesList.stream().anyMatch(snake -> snake.getSnakeBody().headCollision(point)) ||
            AISnakesList.stream().anyMatch(snake -> snake.getSnakeBody().tailCollision(point));
    }

    @Override
    public boolean update() {
        var death = playerSnake.update();
        AISnakesList = AISnakesList.stream().filter(Snake::update)
            .collect(Collectors.toList());

        if (applesSet.size() < maxApples) {
            applesSet.add(new Apple.Factory(this).generateRandom(10000));
            // TODO объединить везде maxIterations, вынести в константу
            // TODO одна фабрика для всех яблок
        }
        // TODO snakes collisions

        return death;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
