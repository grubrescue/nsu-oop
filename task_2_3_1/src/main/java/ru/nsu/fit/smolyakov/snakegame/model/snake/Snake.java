package ru.nsu.fit.smolyakov.snakegame.model.snake;

import ru.nsu.fit.smolyakov.snakegame.model.Apple;
import ru.nsu.fit.smolyakov.snakegame.model.GameModel;
import ru.nsu.fit.smolyakov.snakegame.model.GameModelImpl;
import ru.nsu.fit.smolyakov.snakegame.point.Point;

/**
 * A snake that moves on the {@link GameModel}.
 *
 * <p>(Пока на русском)
 * В целом порядок такой:
 * 1. Вызываем shift(). Изначально все змейки перемещаются в соответствии со своим MovingDirection.
 * 1.1. В случае, если на новом месте головы встретилось яблоко - оно съедается, а хвост не удаляется.
 * Важно упомянуть, что если несколько змей пытаются съесть одно и то же яблоко, то они обе умирают в последующем шаге.
 * 1.2. В случае, если на новом месте головы встретился барьер - змейка умирает.
 * 2. После shift() вызывается checkSnakeCollisions(). Она проверяет, не встретилась ли голова змейки с другими змейками.
 * 2.1. Если голова встретилась с головой - харакири.
 * 2.2. Если голова встретилась с хвостом - змейка обрубается на месте встречи.
 */
public class Snake { // TODO хочется добавить состояние жива/не жива
    private final SnakeBody snakeBody;

    private final GameModel gameModel;

    private MovingDirection movingDirection;
    private int points = 0;

    /**
     * Instantiates a snake in a random position on a field.
     *
     * @param gameModel game field
     */
    public Snake(GameModel gameModel) {
        this.gameModel = gameModel;
        this.snakeBody = SnakeBody.generateRandom(this.gameModel);
        this.movingDirection = MovingDirection.UP;
    }

    /**
     * Sets the direction in which the snake is moving.
     *
     * <p>If incorrect {@code movingDirection} is passed (e.g. {@code LEFT} when the snake
     * is currently moving {@code RIGHT}), then the direction will not be changed.
     *
     * @param movingDirection direction in which the snake is moving
     * @return {@code true} if the direction was changed,
     * {@code false} otherwise
     */
    public boolean setMovingDirection(MovingDirection movingDirection) {
        if (this.getSnakeBody().getTail().get(0).equals(
            this.getSnakeBody().getHead().shift(movingDirection.move()))) {
            return false;
        } else {
            this.movingDirection = movingDirection;
            return true;
        }
    }

    /**
     * Moves the snake in the direction it is currently moving.
     *
     * @return {@code true} if the snake is still alive,
     * {@code false} otherwise
     */
    public boolean update() {
        var newHeadLocation = snakeBody.getHead()
            .shift(movingDirection.move(),
                gameModel.getProperties().width(),
                gameModel.getProperties().height());

        var appleSet = gameModel.getApplesSet();

        var possibleApple = new Apple(newHeadLocation);
        boolean ateApple;

        if (appleSet.contains(possibleApple)) {
            ateApple = true;
            points++;
            appleSet.remove(possibleApple);
        } else {
            ateApple = false;
        }

        snakeBody.move(newHeadLocation, ateApple);
        return !gameModel.getBarrier().met(newHeadLocation)
            && !snakeBody.tailCollision(snakeBody.getHead());
    }

    /**
     * Returns the snake's body, that consists of {@link Point}s.
     *
     * @return snake's body
     */
    public SnakeBody getSnakeBody() {
        return snakeBody;
    }

    /**
     * Returns the snake's amount of points. Currently, those are equal to
     * the amount of apples eaten.
     *
     * @return snake's amount of points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Returns the game field on which the snake is moving.
     *
     * @return game field
     */
    public GameModel getGameField() {
        return gameModel;
    }

    /**
     * Returns the direction in which the snake is moving.
     *
     * @return direction in which the snake is moving
     */
    public MovingDirection getMovingDirection() {
        return movingDirection;
    }

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
         * The snake will only shift along the y-axis by -1.
         */
        UP(new Point(0, -1)),

        /**
         * Represents a direction in which the snake is moving down.
         * The snake will only shift along the y-axis by 1.
         */
        DOWN(new Point(0, 1));

        private final Point move;

        /**
         * Creates a new instance of {@link MovingDirection}
         * with the movement specified by {@code shift}.
         *
         * @param move movement
         */
        MovingDirection(Point move) {
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

        /**
         * Returns the opposite direction.
         *
         * @return opposite direction
         */
        public Point opposite() {
            return new Point(-move.x(), -move.y());
        }
    }
}