//package ru.nsu.fit.smolyakov.snakegame.model.snake.ai.impl;
//
//import ru.nsu.fit.smolyakov.snakegame.model.Apple;
//import ru.nsu.fit.smolyakov.snakegame.model.GameModel;
//import ru.nsu.fit.smolyakov.snakegame.model.snake.Snake;
//import ru.nsu.fit.smolyakov.snakegame.model.snake.SnakeBody;
//import ru.nsu.fit.smolyakov.snakegame.point.Point;
//
//public class SmartAISnake extends GreedyAISnake {
//    /**
//     * {@inheritDoc}
//     *
//     * @param gameModel
//     */
//    public SmartAISnake(GameModel gameModel) {
//        super(gameModel);
//    }
//
//    private Apple target = null;
//
//    protected boolean isTurnOk(int depth, SnakeBody snakeBody, Apple apple) {
//        if (depth == 0) {
//            return true;
//        }
//
//        var xDiff = target.point().x() - getSnakeBody().getHead().x();
//        var yDiff = target.point().y() - getSnakeBody().getHead().y();
//
//        if (snakeBody.headCollision(new Point(xDiff, yDiff))) {
//            return false;
//        }
//
//        if (xDiff > 0 && isNonCollidingTurn(MovingDirection.RIGHT)) {
//            setMovingDirection(MovingDirection.RIGHT);
//        } else if (xDiff < 0 && isNonCollidingTurn(MovingDirection.LEFT)) {
//            setMovingDirection(MovingDirection.LEFT);
//        } else if (yDiff > 0 && isNonCollidingTurn(MovingDirection.DOWN)) {
//            setMovingDirection(MovingDirection.DOWN);
//        } else if (yDiff < 0 && isNonCollidingTurn(MovingDirection.UP)) {
//            setMovingDirection(MovingDirection.UP);
//
//        return isTurnOk(depth - 1, newSnakeBody, apple);
//    }
//
//    protected boolean isTurnOk(int depth, Apple apple) {
//        if (depth == 0) {
//            return true;
//        }
//
//        SnakeBody snakeBody = this.getSnakeBody();
//        return isTurnOk(depth, snakeBody, apple);
//    }
//
//    @Override
//    public void thinkAboutTurn() {
//    }
//}
