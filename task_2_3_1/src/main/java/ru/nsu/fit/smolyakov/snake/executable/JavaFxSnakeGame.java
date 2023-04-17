package ru.nsu.fit.smolyakov.snake.executable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.fit.smolyakov.snake.model.GameField;
import ru.nsu.fit.smolyakov.snake.presenter.Presenter;
import ru.nsu.fit.smolyakov.snake.properties.JavaFxProperties;
import ru.nsu.fit.smolyakov.snake.properties.GameFieldProperties;
import ru.nsu.fit.smolyakov.snake.view.JavaFxView;

import java.io.IOException;

public class JavaFxSnakeGame extends Application {
    private JavaFxProperties javaFxProperties;
    private GameFieldProperties gameFieldProperties;

    private JavaFxView view;
    private GameField model;
    private Presenter presenter;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.javaFxProperties = new JavaFxProperties("Snake JAVAFX", 1600, 900); // TODO хочу вынести в жсон
        this.gameFieldProperties = new GameFieldProperties(16, 9, 3); // TODO хочу вынести в жсон

        var fxmlLoader = new FXMLLoader(getClass().getResource("/gamefield.fxml"));
        Scene rootScene = fxmlLoader.load();
        primaryStage.setScene(rootScene);

        this.view = fxmlLoader.getController();

        this.presenter = new Presenter(this.view, gameFieldProperties);
        this.view.createField(primaryStage, rootScene, gameFieldProperties, javaFxProperties, presenter);

        this.presenter.start();
        primaryStage.show();
    }

    public void execute() {
        launch();
    }
}
