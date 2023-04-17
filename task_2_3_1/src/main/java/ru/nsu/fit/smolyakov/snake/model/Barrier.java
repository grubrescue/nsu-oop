package ru.nsu.fit.smolyakov.snake.model;

import ru.nsu.fit.smolyakov.snake.properties.GameFieldProperties;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a barrier that is located on {@link GameField}.
 * The snake cannot pass through the barrier.
 */
public record Barrier(Set<Point> barrierPoints) {
    public static Barrier fromResource(GameFieldProperties properties) {
        var stream = Objects.requireNonNull(Barrier.class.getResourceAsStream(properties.barrierUrl()));
        var reader = new java.util.Scanner(stream).useDelimiter("\\A");

        Set<Point> points = new HashSet<>();
        for (int y = 0; y < properties.height(); y++) {
            String line = reader.nextLine();
            if (line.length() != properties.width()) {
                throw new IllegalArgumentException("Invalid barrier file");
            }

            for (int x = 0; x < properties.width(); x++) {
                if (line.charAt(x) == '*') {
                    points.add(new Point(x, y));
                }
            }
        }

        return new Barrier(points);
    }

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
