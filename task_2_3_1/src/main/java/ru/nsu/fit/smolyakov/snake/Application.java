package ru.nsu.fit.smolyakov.snake;

import ru.nsu.fit.smolyakov.snake.executable.ConsoleSnakeGame;
import ru.nsu.fit.smolyakov.snake.executable.JavaFxSnakeGame;

import java.io.IOException;


public class Application {
    public static void main(String[] args) {
//        var app = new JavaFxSnakeGame();
//        app.execute();

        var app = new ConsoleSnakeGame();
        try {
            app.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
