package ru.nsu.fit.smolyakov.snakegame;

import ru.nsu.fit.smolyakov.snakegame.configtool.JavaFxConfigTool;
//import ru.nsu.fit.smolyakov.snakegame.executable.ConsoleSnakeGame;
import ru.nsu.fit.smolyakov.snakegame.executable.ConsoleSnakeGame;
import ru.nsu.fit.smolyakov.snakegame.executable.JavaFxSnakeGame;

import java.io.IOException;

/**
 * The entry point of the application. It can be run in two modes: GUI and console.
 * The former is the default one, and can be explicitly specified by passing the
 * {@code --gui} command line argument.
 * The latter can be run by passing the {@code --console} argument.
 * The configuration tool can be run by passing the {@code --config} argument.
 */
public class Application {
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
