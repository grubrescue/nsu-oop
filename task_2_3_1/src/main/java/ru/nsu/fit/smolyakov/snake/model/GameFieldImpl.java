package ru.nsu.fit.smolyakov.snake.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A main model that composes {@link Apple}s and {@link Snake}s, both player- and AI-driven.
 */
public class GameFieldImpl implements GameField {
    private int width;
    private int height;
    private int maxApples;

    private Snake playerSnake;
    private List<Snake> AISnakesList;
    private Set<Apple> applesSet;
    private Barrier barrier;

    /**
     * Returns a list of AI-driven snakes.
     *
     * @return a list of AI-driven snakes
     */
    @Override
    public List<Snake> getAISnakeList() {
        return AISnakesList;
    }

    /**
     * Returns a player-driven snake.
     * @return a player-driven snake
     */
    @Override
    public Snake getPlayerSnake() {
        return playerSnake;
    }

    /**
     * Returns a set of apples.
     *
     * @return a set of apples
     */
    @Override
    public Set<Apple> getApplesSet() {
        return applesSet;
    }

    /**
     * Returns a barrier.
     *
     * @return a barrier
     */
    @Override
    public Barrier getBarrier() {
        return barrier;
    }

    /**
     * Checks if the point is free.
     *
     * @param point point
     * @return {@code true} if the point is free,
     *         {@code false} otherwise
     */
    @Override
    public boolean isFree(Point point) {
        return playerSnake.getSnakeBody().headCollision(point) ||
            playerSnake.getSnakeBody().tailCollision(point) ||
            barrier.met(point) ||
            applesSet.stream().anyMatch(apple -> apple.location().equals(point)) ||
            AISnakesList.stream().anyMatch(snake -> snake.getSnakeBody().headCollision(point)) ||
            AISnakesList.stream().anyMatch(snake -> snake.getSnakeBody().tailCollision(point));
    }

    /**
     * Updates the model.
     *
     * @return {@code true} if the player snake is dead,
     *         {@code false} otherwise
     */
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

    /**
     * Returns the width of the game field.
     *
     * @return the width of the game field
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the game field.
     *
     * @return the height of the game field
     */
    @Override
    public int getHeight() {
        return height;
    }
}