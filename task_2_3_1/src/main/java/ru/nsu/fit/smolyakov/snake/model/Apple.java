package ru.nsu.fit.smolyakov.snake.model;

import ru.nsu.fit.smolyakov.snake.entity.GameField;
import ru.nsu.fit.smolyakov.snake.entity.Point;

import java.security.SecureRandom;

/**
 * Represents an apple, which is located on {@link GameField} and can be
 * eaten by {@link Snake}.
 *
 * @param location
 */
public record Apple (Point location) {
    /**
     * Instantiates an apple in a random position on a field.
     */
    public class Factory {
        private SecureRandom rand = new SecureRandom();
        private GameField gameField;

        public Factory(GameField gameField) {
            this.gameField = gameField;
        }

        public Apple instance() {
            return new Apple(
                new Point(
                    rand.nextInt(gameField.width()),
                    rand.nextInt(gameField.height())
                )
            );
        }
    }

}
