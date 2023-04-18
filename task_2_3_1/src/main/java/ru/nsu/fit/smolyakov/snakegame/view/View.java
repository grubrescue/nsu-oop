package ru.nsu.fit.smolyakov.snakegame.view;

import ru.nsu.fit.smolyakov.snakegame.model.Apple;
import ru.nsu.fit.smolyakov.snakegame.model.Barrier;
import ru.nsu.fit.smolyakov.snakegame.model.snake.Snake;
import ru.nsu.fit.smolyakov.snakegame.presenter.Presenter;

/**
 * Represents a view of the game. One is responsible for drawing the game field
 * and all the objects on it. Though, as we stick to MVP pattern, the view is passive
 * and can only be changed by {@link Presenter}.
 */
public interface View {
    /**
     * Sets the current amount of points the player
     * has on an attached scoreboard.
     *
     * @param scoreAmount the amount of points
     */
    void setScoreAmount(int scoreAmount);

    /**
     * Draws an apple on the game field.
     *
     * @param apple the apple to draw
     */
    void drawApple(Apple apple);

    /**
     * Draws a barrier on the game field.
     *
     * @param barrier the barrier to draw
     */
    void drawBarrier(Barrier barrier);

    /**
     * Draws a snake on the game field.
     *
     * @param snake the snake to draw
     */
    void drawPlayerSnake(Snake snake);

    /**
     * Draws a snake on the game field.
     *
     * @param snake the snake to draw
     */
    void drawEnemySnake(Snake snake);

    /**
     * Shows a message on the attached information panel.
     *
     * @param string the message to show
     */
    void showMessage(String string);

    /**
     * Clears the game field.
     */
    void clear();

    /**
     * Safely closes all running view components.
     */
    void close();
}
