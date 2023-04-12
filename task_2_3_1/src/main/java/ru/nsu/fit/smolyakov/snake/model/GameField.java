package ru.nsu.fit.smolyakov.snake.model;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A main model that composes {@link Apple}s and {@link Snake}s, both player- and AI-driven.
 */
public interface GameField { // TODO разделить интерфейсы
    List<Snake> getAISnakeList();
    Snake getPlayerSnake();
    Set<Apple> getApplesSet();
    Barrier getBarrier();

    boolean isFree(Point point);

    int getWidth();
    int getHeight();

    boolean update();
}
