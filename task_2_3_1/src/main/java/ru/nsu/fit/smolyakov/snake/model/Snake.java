package ru.nsu.fit.smolyakov.snake.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Snake {
    public enum MovingDirection {
        LEFT(-1, 0),
        RIGHT(1, 0),
        UP(0, 1),
        DOWN(0, -1);

        private final int xMove;
        private final int yMove;

        MovingDirection(int xMove, int yMove) {
            this.xMove = xMove;
            this.yMove = yMove;
        }

        public int xMove() {
            return xMove;
        }

        public int yMove() {
            return yMove;
        }
    }

    private List<Point> snakeBody = new LinkedList<>(); // к сожалению, в деке нельзя обращаться
                                                        // к элементам в середине, так что мучаемся с индексами
    private MovingDirection movingDirection;

    private GameModel gameModel;

    public void setMovingDirection(MovingDirection movingDirection) {
        this.movingDirection = movingDirection;
    }

    private Optional<Point> newHeadLocation() {
        var currentHead = snakeBody.get(0);

        var height =

        return new Point(currentHead.x() + movingDirection.xMove(),
            snakeBody.get(0).y() + movingDirection.yMove());
    }

    /**
     *
     * @return head location
     */
    public Point move() {
        var newHeadLocation = new Point(snakeBody.get(0).x() + movingDirection.xMove(),
                                        snakeBody.get(0).y() + movingDirection.yMove());

        // TODO коллизии со стенками

        // if коллизия с другими змейками
        // по идее если змейки столкнулись головами то обе умрут... (грустно)
        // если змейка покушает хвост другой змейки то тот исчезнет просто
        // а еще змейка может сама себя покушать, почему бы и нет
        snakeBody.add(0, newHeadLocation);

        gameModel.getApple().ifPresentOrElse(
            apple -> {
                if (apple.location().equals(newHeadLocation)) {
                    gameModel.eatApple();
                }},

            () -> snakeBody.remove(snakeBody.size() - 1)
        );

        return newHeadLocation;
    }
}