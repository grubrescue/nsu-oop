package ru.nsu.fit.smolyakov.snake;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.fit.smolyakov.snake.executable.JavaFxSnakeGame;
import ru.nsu.fit.smolyakov.snake.view.JavaFxContext;
import ru.nsu.fit.smolyakov.snake.view.JavaFxView;

import javax.naming.Context;
import java.io.IOException;
import java.util.Arrays;

public class Application {
    public static void main(String[] args) {
        var app = new JavaFxSnakeGame();
        app.execute();
    }
}
