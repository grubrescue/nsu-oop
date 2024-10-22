package ru.nsu.fit.smolyakov.snakegame.model.snake;

import ru.nsu.fit.smolyakov.snakegame.model.GameModel;
import ru.nsu.fit.smolyakov.snakegame.model.GameModelImpl;
import ru.nsu.fit.smolyakov.snakegame.utils.Point;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a snake body that consists of a head and a tail. The former is a
 * single point and the latter is a list of points.
 */
public class SnakeBody {
    private Point head;
    private List<Point> tail = new LinkedList<>();

    /**
     * Creates a snake body with the specified head and tail.
     * Typically, this one only used to create a snake body by {@link #generateRandom(GameModel)}.
     *
     * @param head head
     * @param tail tail
     */
    public SnakeBody(Point head, List<Point> tail) {
        if (Objects.requireNonNull(tail).isEmpty()) {
            throw new IllegalArgumentException("tail cannot be empty");
        }
        this.head = Objects.requireNonNull(head);
        this.tail.addAll(tail);
    }

    /**
     * Generates a random snake body, always directed to the top.
     * Amount of iterations is limited by {@link GameModelImpl#MAX_GENERATION_ITERATIONS}.
     *
     * @param gameModel game field
     * @return a random snake body
     */
    public static SnakeBody generateRandom(GameModel gameModel) {
        for (int i = 0; i < GameModelImpl.MAX_GENERATION_ITERATIONS; i++) {
            var initialHeadLocation = Point.random(gameModel.getProperties().width(), gameModel.getProperties().height());
            var initialTailLocation = initialHeadLocation.shift(new Point(0, 1)); // чучуть вниз

            if (gameModel.isFree(initialHeadLocation) && gameModel.isFree(initialTailLocation)) {
                return new SnakeBody(initialHeadLocation, List.of(initialTailLocation));
            }
        }

        throw new IllegalStateException("Cannot create a snake " +
            "(maybe the field is too busy, " +
            "try to increase the amount of iterations, " +
            "increase the field size or remove some barriers)");
    }

    /**
     * Checks whether the head of a snake contains the specified {@code point}.
     *
     * @param point point
     * @return {@code true} if the head of a snake contains the specified {@code point},
     * {@code false} otherwise
     */
    public boolean headCollision(Point point) {
        return head.equals(point);
    }

    /**
     * Checks whether this collision may lead to snake's death.
     *
     * @param point point
     * @return {@code true} if this collision may lead to snake's death,
     * {@code false} otherwise
     */
    public boolean deathCollision(Point point) {
        return head.equals(point) || tail.get(0).equals(point);
    }

    /**
     * Checks whether the tail of a snake contains the specified {@code point}.
     *
     * @param point point
     * @return {@code true} if the tail of a snake contains the specified {@code point},
     * {@code false} otherwise
     */
    public boolean tailCollision(Point point) {
        return tail.contains(point);
    }

    /**
     * Cuts the tail of a snake if it contains the specified {@code point}.
     *
     * @param point point
     * @return {@code true} if the cut is successful,
     * {@code false} otherwise
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
     * @param grow    whether the snake has eaten the apple
     */
    public void move(Point newHead, boolean grow) {
        tail.add(0, head);
        head = newHead;

        if (!grow) {
            tail.remove(tail.size() - 1);
        }
    }

    /**
     * Returns a copy of a snake body.
     *
     * @return a copy of a snake body
     */
    public SnakeBody copy() {
        return new SnakeBody(head, tail);
    }

    /**
     * Returns if this snake body is equal to the specified object.
     * Two snake bodies are equal if they have the same head and tail.
     *
     * @param o object to be compared for equality with this snake body
     * @return {@code true} if this snake body is equal to the specified object,
     * {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null) {
            return false;
        } else if (o instanceof SnakeBody snakeBody) {
            return getHead().equals(snakeBody.getHead()) && getTail().equals(snakeBody.getTail());
        } else {
            return false;
        }
    }

    /**
     * Returns a hash code value for this snake body.
     *
     * @return a hash code value for this snake body
     */
    @Override
    public int hashCode() {
        return Objects.hash(getHead(), getTail());
    }
}
