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
        private final GameField gameField;

        /**
         * Creates a factory for apples connected to the specified {@code gameField}.
         *
         * @param gameField game field
         */
        public Factory(GameField gameField) {
            this.gameField = gameField;
        }

        /**
         * Instantiates an apple in a random position on a field.
         * If the field is too busy, the method will try to
         * create an apple for {@code iterations} times.
         *
         * @param iterations amount of iterations to try to create an apple
         * @return an apple in a random position on a field
         *
         * @throws IllegalStateException if the apple cannot be created
         */
        public Apple generateRandom(int iterations) {
            for (int i = 0; i < iterations; i++) {
                Apple apple = new Apple(Point.random(gameField.getWidth(), gameField.getHeight()));
                if (gameField.isFree(apple.location)) {
                    return apple;
                }
            }

            throw new IllegalStateException("Cannot create an apple " +
                "(maybe the field is too busy, " +
                "try to increase the amount of iterations, " +
                "increase the field size or remove some barriers)");
        }
    }
}
