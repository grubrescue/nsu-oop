package ru.nsu.fit.smolyakov.snake.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a snake body that consists of a head and a tail. The former is a
 * single point and the latter is a list of points.
 */
public class SnakeBody {
    private Point head;
    private List<Point> tail = new LinkedList<>();

    /**
     * Creates a snake body with the specified head and tail.
     * Typically, this one only used to create a snake body by {@link Factory}.
     *
     * @param head head
     * @param tail tail
     */
    protected SnakeBody(Point head, List<Point> tail) {
        this.head = head;
        this.tail.addAll(tail);
    }

    /**
     * Creates a snake body in a random location. One always consists of a head and one tail point
     * and always directed upwards.
     */
    public static class Factory {
        private final GameField gameField;

        /**
         * Creates a new instance of {@link Factory} connected to the specified {@code gameField}.
         * @param gameField game field
         */
        public Factory(GameField gameField) {
            this.gameField = gameField;
        }

        /**
         * Creates a snake body in a random location. One always consists of a head and one tail point.
         *
         * @param iterations amount of iterations to try to create a snake
         * @return a snake body in a random location
         *
         * @throws IllegalStateException if the snake cannot be created
         */
        public SnakeBody generateRandom(int iterations) {
            for (int i = 0; i < iterations; i++) {
                var initialHeadLocation = Point.random(gameField.getWidth(), gameField.getHeight());
                var initialTailLocation = initialHeadLocation.shift(new Point(0, -1)); // чучуть вниз

                if (gameField.isFree(initialHeadLocation) && gameField.isFree(initialTailLocation)) {
                    return new SnakeBody(initialHeadLocation, List.of(initialTailLocation));
                }
            }

            throw new IllegalStateException("Cannot create a snake " +
                "(maybe the field is too busy, " +
                "try to increase the amount of iterations, " +
                "increase the field size or remove some barriers)");
        }
    }

    /**
     * Checks whether the head of a snake contains the specified {@code point}.
     *
     * @param point point
     * @return {@code true} if the head of a snake contains the specified {@code point},
     *         {@code false} otherwise
     */
    public boolean headCollision(Point point) {
        return head.equals(point);
    }

    /**
     * Checks whether the tail of a snake contains the specified {@code point}.
     *
     * @param point point
     * @return {@code true} if the tail of a snake contains the specified {@code point},
     *         {@code false} otherwise
     */
    public boolean tailCollision(Point point) {
        return tail.contains(point);
    }

    /**
     * Cuts the tail of a snake if it contains the specified {@code point}.
     * @param point point
     * @return {@code true} if the cut is successful,
     *         {@code false} otherwise
     */
    public boolean cutTail(Point point) {
        if (tail.contains(point)) {
            tail = tail.subList(0, tail.indexOf(point));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a head of a snake.
     *
     * @return a head of a snake
     */
    public Point getHead() {
        return head;
    }

    /**
     * Returns a tail of a snake.
     *
     * @return a tail of a snake
     */
    public List<Point> getTail() {
        return tail;
    }

    /**
     * Moves the head to a location specified by {@code newHead}.
     * If the snake has eaten the apple, it grows - the last item of a tail remains, and
     * old head {@link Point} becomes a first item of a tail.
     *
     * @param newHead new head location
     * @param grow whether the snake has eaten the apple
     */
    public void move(Point newHead, boolean grow) {
        if (!newHead.connected(head)) {
            throw new IllegalArgumentException("New head must be connected to the tail");
        }

        tail.add(0, head);
        head = newHead;

        if (!grow) {
            tail.remove(tail.size() - 1);
        }
    }
}
