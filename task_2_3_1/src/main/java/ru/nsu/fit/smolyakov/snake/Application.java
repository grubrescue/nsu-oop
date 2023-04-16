package ru.nsu.fit.smolyakov.snake;

import ru.nsu.fit.smolyakov.snake.executable.JavaFxSnakeGame;


public class Application {
    public static void main(String[] args) {
        var app = new JavaFxSnakeGame();
        app.execute();
    }
}
