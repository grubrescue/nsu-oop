package ru.nsu.fit.smolyakov.snake;

import ru.nsu.fit.smolyakov.snake.executable.ConsoleSnakeGame;
import ru.nsu.fit.smolyakov.snake.executable.JavaFxSnakeGame;

import java.io.IOException;


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
    
    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("--gui")) {
            runJavaFxSnakeGame();
        } else if (args[0].equals("--console")) {
            runConsoleSnakeGame();
        } else {
            throw new RuntimeException("unknown argument: " + args[0]);
        }
    }
}
