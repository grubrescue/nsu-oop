package ru.nsu.fit.smolyakov.snake.model;

import java.util.Optional;

/**
 * A snake that moves on the {@link GameField}.
 *
 * (Пока на русском)
 * В целом порядок такой:
 * 1. Вызываем shift(). Изначально все змейки перемещаются в соответствии со своим MovingDirection.
 * 1.1. В случае, если на новом месте головы встретилось яблоко - оно съедается, а хвост не удаляется.
 * Важно упомянуть, что если несколько змей пытаются съесть одно и то же яблоко, то они обе умирают в последующем шаге.
 * 1.2. В случае, если на новом месте головы встретился барьер - змейка умирает.
 * 2. После shift() вызывается checkSnakeCollisions(). Она проверяет, не встретилась ли голова змейки с другими змейками.
 * 2.1. Если голова встретилась с головой - харакири.
 * 2.2. Если голова встретилась с хвостом - змейка обрубается на месте встречи.
 */
public class Snake {
    /**
     * Represents a direction in which the snake is moving.
     * It is used to calculate the next location of the snake's head.
     */
    public enum MovingDirection {
        /**
         * Represents a direction in which the snake is moving to the left.
         * The snake will only shift along the x-axis by -1.
         */
        LEFT(new Point(-1, 0)),

        /**
         * Represents a direction in which the snake is moving to the right.
         * The snake will only shift along the x-axis by 1.
         */
        RIGHT(new Point(1, 0)),

        /**
         * Represents a direction in which the snake is moving up.
         * The snake will only shift along the y-axis by 1.
         */
        UP(new Point(0, 1)),

        /**
         * Represents a direction in which the snake is moving down.
         * The snake will only shift along the y-axis by -1.
         */
        DOWN(new Point(0, -1));

        private final Point move;

        /**
         * Creates a new instance of {@link MovingDirection}
         * with the movement specified by {@code shift}.
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

    private final SnakeBody snakeBody;
    private MovingDirection movingDirection;

    private final GameField gameField;
    private boolean dead = false;

    protected final static int MAX_CREATION_ITERATIONS = 10000; // TODO сделать зависимо от наполненности поля и размера??

    public Snake(GameField gameField) {
        this.gameField = gameField;

        this.snakeBody = new SnakeBody.Factory(gameField).generateRandom(MAX_CREATION_ITERATIONS);
        // TODO сделать шоб одна фабрика на все змейки

        this.movingDirection = MovingDirection.UP;
    }

    public boolean setMovingDirection(MovingDirection movingDirection) {
        if (this.movingDirection.move().shift(movingDirection.move()).equals(Point.ZERO)) {
            return false;
        } else {
            this.movingDirection = movingDirection;
            return true;
        }
    }

    /**
     * Moves the snake in the direction it is currently moving.
     *
     * If the snake meets a
     */
    public boolean update() {
        var newHeadLocation = snakeBody.getHead()
            .shift(movingDirection.move(),
                gameField.getWidth(),
                gameField.getHeight());

        // TODO коллизии со стенками
        if (gameField.getBarrier().met(newHeadLocation)) {
            dead = true;
            return false;
        }

        var apples = gameField.getApplesSet();
        var apple = gameField.getApplesSet().stream().filter(food -> food.location().equals(newHeadLocation)).findAny();

        boolean ateApple = false;
        if (apple.isPresent()) {
            ateApple = true;
            apples.remove(apple.get());
        }

        snakeBody.move(newHeadLocation, ateApple);
    }
}