package ru.nsu.fit.smolyakov.snake;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.fit.smolyakov.snake.view.JavaFxProperties;
import ru.nsu.fit.smolyakov.snake.view.JavaFxView;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private static JavaFxProperties properties;
    private static JavaFxView view;

    @Override
    public void start(Stage primaryStage) throws IOException {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/gamefield.fxml"));

        Scene rootScene = fxmlLoader.load();

        view = (JavaFxView) fxmlLoader.getController();

        primaryStage.setTitle("Snake");
        primaryStage.setWidth(properties.width());
        primaryStage.setHeight(properties.height());
        primaryStage.setResizable(false);
        primaryStage.setScene(rootScene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        properties = new JavaFxProperties(800, 122);
        launch(args);
        view.applyProperties(properties);
    }
}
