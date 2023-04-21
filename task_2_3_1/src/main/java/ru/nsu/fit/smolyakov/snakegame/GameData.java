package ru.nsu.fit.smolyakov.snakegame;

import ru.nsu.fit.smolyakov.snakegame.model.GameModel;
import ru.nsu.fit.smolyakov.snakegame.model.snake.ai.AISnake;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

/**
 * A singleton class that contains all the static data that is used in the game.
 * It is used to avoid hardcoding of paths to game data. This class is used in the game
 * to load game data from files.
 */
public enum GameData {
    /**
     * The only instance of this class.
     */
    INSTANCE;

    /**
     * The title of the game. Usually, one is used in the window title in JavaFx implementation.
     */
    public final String GAME_TITLE = "Snake Game";

    /**
     * The path to the folder with game data.
     */
    public final String GAMEDATA_FOLDER_PATH = "gamedata/";

    /**
     * The path to the folder with game configuration files.
     */
    public final String CONFIG_FOLDER_PATH = GAMEDATA_FOLDER_PATH + "config/";

    /**
     * The path to the file with game properties. Currently, there is only one file with game properties,
     * but potentially it is possible to have multiple files with different game properties.
     */
    public final String GAME_PROPERTIES_YAML_PATH = CONFIG_FOLDER_PATH + "gameProperties.yaml";

    /**
     * The path to the folder with level files.
     */
    public final String LEVEL_FOLDER_PATH = GAMEDATA_FOLDER_PATH + "level/";

    /**
     * The path to the folder with AI snakes.
     */
    public final String AI_SNAKES_PACKAGE_NAME = "ru.nsu.fit.smolyakov.snakegame.model.snake.ai.impl";

    /**
     * Returns the list of names of all AI snakes.
     * In this case, AI name is defined as the part of the class name after the last dot: for example,
     * short name for {@code ru.nsu.fit.smolyakov.snakegame.model.snake.ai.impl.ExampleAISnake}
     * is {@code ExampleAISnake}.
     *
     * <p>This method is used to populate the list of AI snakes in {@link ru.nsu.fit.smolyakov.snakegame.configtool}.
     *
     * <p>Actual path to the package with AI snakes is defined by {@link #AI_SNAKES_PACKAGE_NAME}.
     *
     * @return list of AI snake names
     * @see ru.nsu.fit.smolyakov.snakegame.model.snake.ai.impl
     */
    public List<String> getAvailableAiNames() {
        try (InputStream stream = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream(AI_SNAKES_PACKAGE_NAME.replace('.', '/'))) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(stream)));
            return reader.lines()
                .filter(str -> str.endsWith(".class"))
                .map(str -> str.substring(0, str.length() - 6))
                .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns an instance of {@link AISnake} with the given short class name.
     * Short class name is defined as the part of the class name after the last dot: for example,
     * short name for {@code ru.nsu.fit.smolyakov.snakegame.model.snake.ai.impl.ExampleAISnake}
     * is {@code ExampleAISnake}.
     *
     * <p>Actual path to the package with AI snakes is defined by {@link #AI_SNAKES_PACKAGE_NAME}.
     *
     * @param shortClassName short class name of the AI snake
     * @param gameModel      game model that is passed to the constructor of the AI snake
     * @return {@link Optional} with an instance of {@link AISnake} if the class with the given name
     * exists and is a subclass of {@link AISnake}, or {@link Optional#empty()} otherwise.
     */
    public Optional<AISnake> aiSnakeByShortName(String shortClassName, GameModel gameModel) {
        Class<?> aiSnakeClass;

        try {
            aiSnakeClass = Class.forName(AI_SNAKES_PACKAGE_NAME + "." + shortClassName);
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }

        if (!AISnake.class.isAssignableFrom(aiSnakeClass)) {
            return Optional.empty();
        }

        AISnake aiSnake;
        try {
            aiSnake = (AISnake) aiSnakeClass.getDeclaredConstructor(GameModel.class).newInstance(gameModel);
        } catch (InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException
                 | NoSuchMethodException e) {
            return Optional.empty();
        }

        return Optional.of(aiSnake);
    }

    /**
     * Returns the list of names of all level files. Ones are searched in the {@link #LEVEL_FOLDER_PATH}.
     *
     * @return list of level file names
     */
    public List<String> levelFileNames() {
        try (var barrierPathsList = Files.list(Paths.get(LEVEL_FOLDER_PATH))) {
            return barrierPathsList
                .map(Path::getFileName)
                .map(Path::toString)
                .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the {@link java.util.Scanner} of the file with the given name.
     * One is searched in the {@link #LEVEL_FOLDER_PATH}.
     *
     * @param levelFilename name of the file with the level
     * @return {@link Optional} of {@link java.util.Scanner} instance associated
     * with the file with the given name if one exists,
     * {@link Optional#empty()} otherwise
     */
    public Optional<Scanner> levelFileScanner(String levelFilename) {
        var file = new File(LEVEL_FOLDER_PATH + levelFilename);
        try {
            Scanner scanner = new Scanner(file).useDelimiter("\\A");
            return Optional.of(scanner);
        } catch (FileNotFoundException e) {
            return Optional.empty();
        }
    }
}
