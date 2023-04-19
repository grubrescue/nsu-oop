package ru.nsu.fit.smolyakov.snakegame.executable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.nsu.fit.smolyakov.snakegame.model.GameModel;
import ru.nsu.fit.smolyakov.snakegame.model.GameModelImpl;
import ru.nsu.fit.smolyakov.snakegame.presenter.SnakePresenter;
import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;
import ru.nsu.fit.smolyakov.snakegame.view.JavaFxView;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * The class that executes the JavaFX variant of the snake game.
 * It creates the model, view and snakePresenter and connects them.
 */
public class JavaFxSnakeGame extends Application {
    private JavaFxView view;
    private GameModel model;
    private SnakePresenter snakePresenter;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        var gameProperties =
            new ObjectMapper(new YAMLFactory()).readValue(
                new File("gamedata/config/gameProperties.yaml"),
                GameProperties.class
        );

        var fxmlLoader = new FXMLLoader(getClass().getResource("/gamefield.fxml"));
        Scene rootScene = fxmlLoader.load();

        primaryStage.setScene(rootScene);
        primaryStage.initStyle(StageStyle.UNDECORATED);

        this.view = fxmlLoader.getController();
        this.model = new GameModelImpl(gameProperties);
        this.snakePresenter = new SnakePresenter(this.view, this.model, gameProperties);

        this.view.initializeField(gameProperties, snakePresenter);

        this.snakePresenter.start();
        primaryStage.show();
    }

    /**
     * Executes the game.
     */
    public void execute() {
        launch();
    }
}
