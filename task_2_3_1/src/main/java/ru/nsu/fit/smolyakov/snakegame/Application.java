package ru.nsu.fit.smolyakov.snakegame;

import ru.nsu.fit.smolyakov.snakegame.configtool.JavaFxConfigTool;
import ru.nsu.fit.smolyakov.snakegame.executable.ConsoleSnakeGame;
import ru.nsu.fit.smolyakov.snakegame.executable.JavaFxSnakeGame;

import java.io.IOException;

/**
 * The entry point of the application. It can be run in two modes: GUI and console.
 * The former is the default one, and can be explicitly specified by passing the
 * {@code --gui} command line argument.
 * The latter can be run by passing the {@code --console} argument.
 */
public class Application {
    public static final String GAMEDATA_FOLDER_PATH = "gamedata/";
    public static final String CONFIG_FOLDER_PATH = GAMEDATA_FOLDER_PATH + "config/";
    public static final String GAME_PROPERTIES_YAML_PATH = CONFIG_FOLDER_PATH + "gameProperties.yaml";
    public static final String LEVEL_FOLDER_PATH = GAMEDATA_FOLDER_PATH + "level/";
    public static final String AI_SNAKES_CLASS_PATH = "ru.nsu.fit.smolyakov.snakegame.ai.";
    public static final String AI_SNAKES_FOLDER_PATH = "src/main/java/ru/nsu/fit/smolyakov/snakegame/ai/";


    private static void runJavaFxSnakeGame() {
        var app = new JavaFxSnakeGame();
        app.execute();
    }
    
    private static void runConsoleSnakeGame() {
        var app = new ConsoleSnakeGame();
        try {
            app.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void runJavaFxConfigTool() {
        var app = new JavaFxConfigTool();
        app.execute();
    }

    /**
     * The entry point of the application.
     *
     * @param args the command line arguments
     * @throws RuntimeException if an unknown argument is passed
     */
    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("--gui")) {
            runJavaFxSnakeGame();
        } else if (args[0].equals("--console")) {
            runConsoleSnakeGame();
        } else if (args[0].equals("--config")) {
            runJavaFxConfigTool();
        } else {
            throw new RuntimeException("unknown argument: " + args[0]);
        }
    }
}
