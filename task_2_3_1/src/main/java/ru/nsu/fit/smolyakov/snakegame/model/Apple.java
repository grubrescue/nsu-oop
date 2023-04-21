package ru.nsu.fit.smolyakov.snakegame.model;

import ru.nsu.fit.smolyakov.snakegame.model.snake.Snake;
import ru.nsu.fit.smolyakov.snakegame.point.Point;

/**
 * Represents an apple, which is located on {@link GameModel} and can be
 * eaten by {@link Snake}.
 */
public record Apple(Point point) {
    /**
     * Instantiates an apple in a random position on a field.
     */
    public static class Factory {
        private final GameModel gameModel;
        private final int iterations;

        /**
         * Creates a factory for apples connected to the specified {@code gameModel}.
         *
         * @param gameModel  game field
         * @param iterations amount of iterations to try to create an apple
         */
        public Factory(GameModel gameModel, int iterations) {
            this.gameModel = gameModel;
            this.iterations = iterations;
        }

        /**
         * Instantiates an apple in a random position on a field.
         * If the field is too busy, the method will try to
         * create an apple for {@code iterations} times.
         *
         * @return an apple in a random position on a field
         * @throws IllegalStateException if the apple cannot be created
         */
        public Apple generateRandom() {
            for (int i = 0; i < iterations; i++) {
                Apple apple = new Apple(Point.random(gameModel.getProperties().width(), gameModel.getProperties().height()));
                if (gameModel.isFree(apple.point)) {
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
