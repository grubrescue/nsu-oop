package ru.nsu.fit.smolyakov.snake.model;

import java.util.Set;

/**
 * Represents a barrier that is located on {@link GameField}.
 * The snake cannot pass through the barrier.
 */
public record Barrier(Set<Point> barrierPoints){
    /**
     * Checks if the point is located on the barrier.
     *
     * @param point point
     * @return {@code true} if the point is located on the barrier, false otherwise
     */
    public boolean met(Point point) {
        return barrierPoints.contains(point);
    }
}
