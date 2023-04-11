package ru.nsu.fit.smolyakov.snake.model;

import java.security.SecureRandom;

/**
 * Represents an apple, which is located on {@link GameField} and can be
 * eaten by {@link Snake}.
 *
 * @param location
 */
public record Apple(Point location) {
    /**
     * Instantiates an apple in a random position on a field.
     */
    public static class Factory {
        private SecureRandom rand = new SecureRandom();
        private final GameField gameField;

        public Factory(GameField gameField) {
            this.gameField = gameField;
        }

        public Apple instance() {
            return new Apple(Point.random(gameField.getWidth(), gameField.getHeight()));
        }
    }
}
