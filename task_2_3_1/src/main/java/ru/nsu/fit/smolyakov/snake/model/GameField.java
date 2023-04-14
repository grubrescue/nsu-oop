package ru.nsu.fit.smolyakov.snake.model;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A main model that composes {@link Apple}s and {@link Snake}s, both player- and AI-driven.
 */
public interface GameField { // TODO разделить интерфейсы
    /**
     * Returns a list of AI-driven snakes.
     *
     * @return a list of AI-driven snakes
     */
    List<Snake> getAISnakeList();

    /**
     * Returns a player-driven snake.
     *
     * @return a player-driven snake
     */
    Snake getPlayerSnake();

    /**
     * Returns a set of apples.
     *
     * @return a set of apples
     */
    Set<Apple> getApplesSet();

    /**
     * Returns a barrier.
     *
     * @return a barrier
     */
    Barrier getBarrier();

    /**
     * Checks if the point is free.
     *
     * @param point point
     * @return {@code true} if the point is free,
     *         {@code false} otherwise
     */
    boolean isFree(Point point);

    /**
     * Returns the width of the game field.
     *
     * @return the width of the game field
     */
    int getWidth();

    /**
     * Returns the height of the game field.
     *
     * @return the height of the game field
     */
    int getHeight();

    /**
     * Updates the model.
     *
     * @return {@code true} if the player snake is dead,
     *         {@code false} otherwise
     */
    boolean update();
}