package ru.nsu.fit.smolyakov.snake.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * A snake that moves on the {@link GameField}.
 *
 */
public class Snake {
    /**
     * Represents a direction in which the snake is moving.
     * It is used to calculate the next location of the snake's head.
     */
    public enum MovingDirection {
        /**
         * Represents a direction in which the snake is moving to the left.
         * The snake will only move along the x-axis by -1.
         */
        LEFT(new Point(-1, 0)),

        /**
         * Represents a direction in which the snake is moving to the right.
         * The snake will only move along the x-axis by 1.
         */
        RIGHT(new Point(1, 0)),

        /**
         * Represents a direction in which the snake is moving up.
         * The snake will only move along the y-axis by 1.
         */
        UP(new Point(0, 1)),

        /**
         * Represents a direction in which the snake is moving down.
         * The snake will only move along the y-axis by -1.
         */
        DOWN(new Point(0, -1));

        private final Point move;

        /**
         * Creates a new instance of {@link MovingDirection}
         * with the movement specified by {@code move}.
         *
         * @param move movement
         */
        private MovingDirection(Point move) {
            this.move = move;
        }

        /**
         * Returns the movement of the snake.
         *
         * @return movement of the snake
         */
        public Point move() {
            return move;
        }
    }

    private final List<Point> snakeBody = new LinkedList<>(); // к сожалению, в деке нельзя обращаться
                                                        // к элементам в середине, так что мучаемся с индексами
    private MovingDirection movingDirection;

    private final GameField gameField;
    private boolean dead = false;


    public Snake(GameField gameField) {
        this.gameField = gameField;

        var initialHeadLocation = Point.random(gameField.getWidth(), gameField.getHeight());
        var initialMovement =

        snakeBody.add(initialHeadLocation);

    }

    public void setMovingDirection(MovingDirection movingDirection) {
        this.movingDirection = movingDirection;
    }

    private Optional<Point> newHeadLocation() {
        var currentHead = snakeBody.get(0);

        var x = (currentHead.x() + movingDirection.xMove()) % gameField.getWidth();
        var y = (currentHead.y() + movingDirection.yMove()) % gameField.getHeight();

        var newHeadLocation = new Point(x, y);
        if (newHeadLocation.equals(snakeBody.get(1))) {
            return Optional.empty();
        } else {
            return Optional.of(newHeadLocation);
        }
    }

    /**
     * Moves the snake in the direction it is currently moving.
     *
     * If the snake meets a
     */
    public boolean move() {
        var newHeadLocation = newHeadLocation();

        // TODO коллизии со стенками

        // if коллизия с другими змейками
        // по идее если змейки столкнулись головами то обе умрут... (грустно)
        // если змейка покушает хвост другой змейки то тот исчезнет просто
        // а еще змейка может сама себя покушать, почему бы и нет
        newHeadLocation.if
        snakeBody.add(0, newHeadLocation);

        gameField.getApple().ifPresentOrElse(
            apple -> {
                if (apple.location().equals(newHeadLocation)) {
                    gameField.eatApple();
                }},

            () -> snakeBody.remove(snakeBody.size() - 1)
        );

        return newHeadLocation;
    }

    public Point head() {
        return snakeBody.get(0);
    }

    public List<Point> getSnakeBody() {
        return snakeBody;
    }


}