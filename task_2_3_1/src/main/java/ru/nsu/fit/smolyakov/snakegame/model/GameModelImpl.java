package ru.nsu.fit.smolyakov.snakegame.model;

import ru.nsu.fit.smolyakov.snakegame.GameData;
import ru.nsu.fit.smolyakov.snakegame.model.snake.CollisionSolver;
import ru.nsu.fit.smolyakov.snakegame.model.snake.Snake;
import ru.nsu.fit.smolyakov.snakegame.model.snake.ai.AISnake;
import ru.nsu.fit.smolyakov.snakegame.point.Point;
import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A main model that composes {@link Apple}s and {@link Snake}s, both player- and AI-driven.
 * It also contains a {@link Barrier} that is a reason of collisions that may lead to
 * one (or all) of snakes' death.
 *
 * <p>As specified in {@link GameProperties} (usually deserialized from {@code gamedata/config/gameProperties.yaml}
 * file), this class dynamically loads {@link AISnake}s that are capable of playing the game, as well as the user.
 *
 * <p>All other properties also specified in {@link GameProperties}, such as {@code apples},
 * {@code width} and {@code height} are used to initialize the game field and affect the representation of the latter.
 */
public class GameModelImpl implements GameModel {
    /**
     * The maximum number of attempts to generate a random object that does
     * not collide with already existing ones.
     */
    public final static int MAX_GENERATION_ITERATIONS = 5000;

    private final GameProperties properties;

    private final Snake playerSnake;
    private final Set<Apple> applesSet;
    private final Barrier barrier;
    private final Apple.Factory appleFactory = new Apple.Factory(this, MAX_GENERATION_ITERATIONS);
    private final List<AISnake> aiSnakesList = new LinkedList<>();

    /**
     * Creates a game field with the specified properties.
     *
     * @param properties properties of the game field
     */
    public GameModelImpl(GameProperties properties) {
        this.properties = properties;
        this.barrier = Barrier.fromResource(properties);

        this.playerSnake = new Snake(this);

        properties.aiClassNamesList()
            .stream()
            .map(name -> GameData.INSTANCE.aiSnakeByShortName(name, this))
            .flatMap(Optional::stream)
            .forEach(aiSnakesList::add);

        this.applesSet = new HashSet<>();
        fulfilApplesSet();
    }

    private void fulfilApplesSet() {
        while (applesSet.size() < properties.apples()) {
            applesSet.add(appleFactory.generateRandom());
        }
    }

    /**
     * Returns a list of AI-driven snakes.
     *
     * @return a list of AI-driven snakes
     */
    @Override
    public List<AISnake> getAISnakeList() {
        return aiSnakesList;
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
        return (playerSnake == null || !playerSnake.getSnakeBody().headCollision(point))
            && (playerSnake == null || !playerSnake.getSnakeBody().tailCollision(point))
            && (barrier == null || !barrier.met(point))
            && (applesSet == null || !applesSet.contains(new Apple(point)))
            && (aiSnakesList == null
            || aiSnakesList.stream().noneMatch(snake -> snake.getSnakeBody().headCollision(point)))
            && (aiSnakesList == null
            || aiSnakesList.stream().noneMatch(snake -> snake.getSnakeBody().tailCollision(point)));
    }

    /**
     * Checks if the player snake collides with any AI snake.
     *
     * @return {@code true} if the player snake collides with any AI snake,
     * {@code false} otherwise
     */
    private boolean checkPlayerSnakeCollisions() {
        var iter = aiSnakesList.iterator();
        while (iter.hasNext()) {
            var snake = iter.next();
            if (CollisionSolver.solve(playerSnake, snake) == CollisionSolver.Result.BOTH_DEAD) {
                iter.remove();
                return true;
            }
        }

        return false;
    }



    /**
     * Updates the model.
     *
     * @return {@code true} if the player snake is dead,
     * {@code false} otherwise
     */
    @Override
    public boolean update() {
        var alive = playerSnake.update();
//        aiSnakesList = aiSnakesList.stream().filter(Snake::update)
//            .collect(Collectors.toList());
//
        aiSnakesList.removeIf(snake -> !snake.update());

        fulfilApplesSet();

        var collisions = checkPlayerSnakeCollisions();
        return alive && !collisions;
    }

    /**
     * Returns the properties of the game field.
     *
     * @return the properties of the game field
     */
    public GameProperties getProperties() {
        return properties;
    }

    /**
     * Returns a new game field with the same properties as this one.
     *
     * @return a new game field with the same properties as this one
     */
    @Override
    public GameModel newGame() {
        return new GameModelImpl(properties);
    }
}
