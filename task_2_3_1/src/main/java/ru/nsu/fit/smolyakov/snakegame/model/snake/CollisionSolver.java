package ru.nsu.fit.smolyakov.snakegame.model.snake;

// TODO возможно эта штука временно тут
public class CollisionSolver {
    public enum Result {
//        FIRST_DEAD,
//        SECOND_DEAD,
        BOTH_DEAD,
        BOTH_ALIVE
    }

    public static Result solve(Snake first, Snake second) {
        // TODO а точно надо отдельный класс для этого?
        // и возвращать булин в целом ок было бы
        var firstBody = first.getSnakeBody();
        var secondBody = second.getSnakeBody();

        if (firstBody.headCollision(secondBody.getHead())
            || firstBody.headCollision(secondBody.getTail().get(0))
            || secondBody.headCollision(firstBody.getTail().get(0))) {
            return Result.BOTH_DEAD;
        }

        if (firstBody.tailCollision(secondBody.getHead())) {
            firstBody.cutTail(secondBody.getHead());
            return Result.BOTH_ALIVE;
        }
        if (secondBody.tailCollision(firstBody.getHead())) {
            secondBody.cutTail(firstBody.getHead());
            return Result.BOTH_ALIVE;
        }

        return Result.BOTH_ALIVE;
    }
}
