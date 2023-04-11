package ru.nsu.fit.smolyakov.snake.model;

import java.security.SecureRandom;

/**
 * Represents a point on the {@link GameField}.
 * In case of {@link ru.nsu.fit.smolyakov.snake} and inner packages, the coordinates are
 * supposed to be non-negative.
 *
 * @param x x-coordinate
 * @param y y-coordinate
 */
public record Point(int x, int y) {
    /**
     * Instantiates a point in a random position with {@code x} and {@code y} coordinates
     * exclusively less than respectively specified {@code xLimit} and {@code yLimit} and
     * inclusively greater than 0.
     *
     * @param xLimit x-coordinate limit
     * @param yLimit y-coordinate limit
     * @return a point with random coordinates within specified limits
     */
    public static Point random(int xLimit, int yLimit) {
        SecureRandom rand = new SecureRandom();
        return new Point(rand.nextInt(xLimit), rand.nextInt(yLimit));
    }

    /**
     * Returns a new point that is moved by {@code move} from the current point.
     *
     * @param move movement
     * @return a new point that is moved by {@code move} from the current point
     */
    public Point move(Point move) {
        return new Point(x + move.x, y + move.y);
    }

    /**
     * Returns a new point that is moved by {@code move} from the current point.
     * If resulting point is out of the field with specified {@code xLimit} and {@code yLimit},
     * it will be moved to the opposite side of the field.
     *
     * @param move movement
     * @param xLimit x-coordinate limit
     * @param yLimit y-coordinate limit
     * @return a new point that is moved by {@code move} from the current point
     */
    public Point move(Point move, int xLimit, int yLimit) {
        if (xLimit <= 0 || yLimit <= 0) {
            throw new IllegalArgumentException("Limits must be positive");
        }

        return new Point(
            (x + move.x + xLimit) % xLimit,
            (y + move.y + yLimit) % yLimit
        );
    }
}