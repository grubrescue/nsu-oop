package ru.nsu.fit.smolyakov.snake.model;

import ru.nsu.fit.smolyakov.snake.entity.Point;

import java.util.LinkedList;
import java.util.List;

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


    private List<Point> snakeBody = new LinkedList<>();
    private MovingDirection movingDirection;

    private GameModelImpl gameModelImpl;

    public void setMovingDirection(MovingDirection movingDirection) {
        this.movingDirection = movingDirection;
    }

    public void move() {
        var newHeadLocation = new Point(snakeBody.get(0).x() + movingDirection.xMove(),
                                        snakeBody.get(0).y() + movingDirection.yMove());

        if (gameModelImpl.)
    }




}
