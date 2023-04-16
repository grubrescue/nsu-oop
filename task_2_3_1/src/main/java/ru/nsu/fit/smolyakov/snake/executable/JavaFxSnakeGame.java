package ru.nsu.fit.smolyakov.snake.executable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.fit.smolyakov.snake.model.GameField;
import ru.nsu.fit.smolyakov.snake.model.GameFieldImpl;
import ru.nsu.fit.smolyakov.snake.presenter.Presenter;
import ru.nsu.fit.smolyakov.snake.view.JavaFxContext;
import ru.nsu.fit.smolyakov.snake.view.JavaFxView;

import java.io.IOException;

public class JavaFxSnakeGame extends Application {
    private JavaFxContext context;
    private JavaFxView view;
    private GameField model;
    private Presenter presenter;

    @Override
    public void start(Stage primaryStage) throws IOException {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/gamefield.fxml"));

        Scene rootScene = fxmlLoader.load();
        primaryStage.setScene(rootScene);

        var context = new JavaFxContext("Snake JAVAFX", 1600, 900, 16, 9); // TODO хочу вынести в жсон

        this.view = fxmlLoader.getController();

        this.presenter = new Presenter(this.view, context.gameFieldWidth(), context.gameFieldHeight());
        this.presenter.start();

        this.view.createField(primaryStage, rootScene, context, presenter);


        primaryStage.show();
    }

    public void execute() {
        launch();
    }
}
