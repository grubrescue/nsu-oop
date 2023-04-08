package ru.nsu.fit.smolyakov.snake.model;

import java.util.List;
import java.util.Optional;

/**
 * A main model that composes {@link Apple} and {@link Snake}'s, both player- and AI-driven.
 */
public interface GameModel {
    List<Snake> getAISnakes();
    Snake getPlayerSnake();
    Optional<Apple> getApple();
}
