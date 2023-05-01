package ru.nsu.fit.smolyakov.snakegame.model.snake;

// TODO возможно эта штука временно тут

/**
 * Solves collisions between two snakes.
 */
public class CollisionSolver {
    /**
     * Solves collisions between two snakes.
     * Cuts the snakes, if the collision without casualties
     * happened.
     *
     * @param firstBody  the first snake body
     * @param secondBody the second snake body
     * @return a result of collision solving
     */
    public static Result solve(SnakeBody firstBody, SnakeBody secondBody) {
        if (firstBody.deathCollision(secondBody.getHead())
            || secondBody.deathCollision(firstBody.getHead())) {
            return Result.BOTH_DEAD;
        }

        if (firstBody.tailCollision(secondBody.getHead())) {
            firstBody.cutTail(secondBody.getHead());
        }
        if (secondBody.tailCollision(firstBody.getHead())) {
            secondBody.cutTail(firstBody.getHead());
        }

        return Result.BOTH_ALIVE;
    }



    /**
     * A result of collision solving.
     */
    public enum Result {
//        /**
//         * The first snake is dead.
//         */
//        FIRST_DEAD,
//
//        /**
//         * The second snake is dead.
//         */
//        SECOND_DEAD,
        /**
         * Both snakes are dead.
         */
        BOTH_DEAD,

        /**
         * Both snakes are alive.
         */
        BOTH_ALIVE
    }
}
