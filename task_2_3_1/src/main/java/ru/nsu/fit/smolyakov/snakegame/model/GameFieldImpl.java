package ru.nsu.fit.smolyakov.snakegame.model;

import ru.nsu.fit.smolyakov.snakegame.model.snake.CollisionSolver;
import ru.nsu.fit.smolyakov.snakegame.model.snake.Snake;
import ru.nsu.fit.smolyakov.snakegame.model.snake.ai.*;
import ru.nsu.fit.smolyakov.snakegame.point.Point;
import ru.nsu.fit.smolyakov.snakegame.properties.GameFieldProperties;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A main model that composes {@link Apple}s and {@link Snake}s, both player- and AI-driven.
 */
public class GameFieldImpl implements GameField {
    private final GameFieldProperties properties;

    private final Snake playerSnake;
    private final Set<Apple> applesSet;
    private final Barrier barrier;
    private List<AISnake> AISnakesList;

    /**
     * Creates a game field with the specified properties.
     *
     * @param properties properties of the game field
     */
    public GameFieldImpl(GameFieldProperties properties) {
        this.properties = properties;
        this.barrier = Barrier.fromResource(properties);

        this.playerSnake = new Snake(this);
        this.AISnakesList = List.of(
            new StraightForwardAISnake(this),
            new TotallyRandomAISnake(this),
            new NotWonnaDieAISnake(this)
        ); // TODO вынести в конструктор?

        this.applesSet = new HashSet<>(); // TODO сделать нормально
        while (applesSet.size() < properties.maxApples()) {
            applesSet.add(new Apple.Factory(this).generateRandom(10000)); // TODO перенести макситерации куда нибудь
        }
    }

    /**
     * Returns a list of AI-driven snakes.
     *
     * @return a list of AI-driven snakes
     */
    @Override
    public List<AISnake> getAISnakeList() {
        return AISnakesList;
    }

    /**
     * Returns a player-driven snake.
     *
     * @return a player-driven snake
     */
    @Override
    public Snake getPlayerSnake() {
        return playerSnake;
    }

    /**
     * Returns a set of apples.
     *
     * @return a set of apples
     */
    @Override
    public Set<Apple> getApplesSet() {
        return applesSet;
    }

    /**
     * Returns a barrier.
     *
     * @return a barrier
     */
    @Override
    public Barrier getBarrier() {
        return barrier;
    }

    /**
     * Checks if the point is free.
     *
     * @param point point
     * @return {@code true} if the point is free,
     * {@code false} otherwise
     */
    @Override
    public boolean isFree(Point point) {
        return (playerSnake == null || !playerSnake.getSnakeBody().headCollision(point)) &&
            (playerSnake == null || !playerSnake.getSnakeBody().tailCollision(point)) &&
            (barrier == null || !barrier.met(point)) &&
            (applesSet == null || !applesSet.contains(new Apple(point))) &&
            (AISnakesList == null || AISnakesList.stream().noneMatch(snake -> snake.getSnakeBody().headCollision(point))) &&
            (AISnakesList == null || AISnakesList.stream().noneMatch(snake -> snake.getSnakeBody().tailCollision(point)));
    }

    /**
     * Checks if the player snake collides with any AI snake.
     *
     * @return {@code true} if the player snake collides with any AI snake,
     *         {@code false} otherwise
     */
    private boolean checkPlayerSnakeCollisions() {
        var iter = AISnakesList.iterator();
        while (iter.hasNext()) {
            var snake = iter.next();
            // коллизион солвер рубит змейку если там коллизия какая
            if (CollisionSolver.solve(playerSnake, snake) == CollisionSolver.Result.BOTH_DEAD) {
                iter.remove();
                return true;
            }
        }

        // TODO змейки между собой

        return false;
    }

    /**
     * Updates the model.
     *
     * @return {@code true} if the player snake is dead,
     *         {@code false} otherwise
     */
    @Override
    public boolean update() {
        var alive = playerSnake.update();
        AISnakesList = AISnakesList.stream().filter(Snake::update)
            .collect(Collectors.toList());

        while (applesSet.size() < properties.maxApples()) {
            applesSet.add(new Apple.Factory(this).generateRandom(10000));
            // TODO объединить везде maxIterations, вынести в константу
            // TODO одна фабрика для всех яблок
        }

        var collisions = checkPlayerSnakeCollisions();
        return alive && !collisions; // TODO переименовать все
    }

    /**
     * Returns the properties of the game field.
     *
     * @return the properties of the game field
     */
    public GameFieldProperties getProperties() {
        return properties;
    }

    /**
     * Returns a new game field with the same properties as this one.
     * @return a new game field with the same properties as this one
     */
    @Override
    public GameField newGame() {
        return new GameFieldImpl(properties);
    }
}
