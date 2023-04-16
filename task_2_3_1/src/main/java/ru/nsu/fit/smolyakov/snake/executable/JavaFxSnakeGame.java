package ru.nsu.fit.smolyakov.snake.executable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.fit.smolyakov.snake.model.GameField;
import ru.nsu.fit.smolyakov.snake.view.JavaFxContext;
import ru.nsu.fit.smolyakov.snake.view.JavaFxView;

import java.io.IOException;

public class JavaFxSnakeGame extends Application {
    private JavaFxContext context;
    private JavaFxView view;
    private GameField gameField;

    @Override
    public void start(Stage primaryStage) throws IOException {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/gamefield.fxml"));

        Scene rootScene = fxmlLoader.load();

        var context = new JavaFxContext("Snake JAVAFX", 1600, 900, 16, 9); // TODO хочу вынести в жсон

        this.view = fxmlLoader.getController();
        this.view.createField(primaryStage, context);

        primaryStage.setScene(rootScene);
        primaryStage.show();
    }

    public void execute() {
        launch();
    }
}
