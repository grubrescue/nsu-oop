package ru.nsu.fit.smolyakov.snakegame.executable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.nsu.fit.smolyakov.snakegame.model.GameField;
import ru.nsu.fit.smolyakov.snakegame.model.GameFieldImpl;
import ru.nsu.fit.smolyakov.snakegame.presenter.Presenter;
import ru.nsu.fit.smolyakov.snakegame.properties.GameFieldProperties;
import ru.nsu.fit.smolyakov.snakegame.properties.JavaFxProperties;
import ru.nsu.fit.smolyakov.snakegame.properties.PresenterProperties;
import ru.nsu.fit.smolyakov.snakegame.view.JavaFxView;

import java.io.IOException;
import java.util.Objects;

/**
 * The class that executes the JavaFX variant of the snake game.
 * It creates the model, view and presenter and connects them.
 */
public class JavaFxSnakeGame extends Application {
    private JavaFxView view;
    private GameField model;
    private Presenter presenter;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        var mapper = new ObjectMapper(new YAMLFactory());

        var javaFxProperties = mapper.readValue(
            Objects.requireNonNull(getClass().getResourceAsStream("/java_fx_properties.yaml")),
            JavaFxProperties.class
        );
        var gameFieldProperties = mapper.readValue(
            Objects.requireNonNull(getClass().getResourceAsStream("/game_field_properties.yaml")),
            GameFieldProperties.class
        );
        var presenterProperties = mapper.readValue(
            Objects.requireNonNull(getClass().getResourceAsStream("/presenter_properties.yaml")),
            PresenterProperties.class
        );

        var fxmlLoader = new FXMLLoader(getClass().getResource("/gamefield.fxml"));
        Scene rootScene = fxmlLoader.load();

        primaryStage.setScene(rootScene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setWidth(javaFxProperties.resX());
        primaryStage.setHeight(javaFxProperties.resY());

        this.view = fxmlLoader.getController();
        this.model = new GameFieldImpl(gameFieldProperties);
        this.presenter = new Presenter(this.view, this.model, presenterProperties);

        this.view.initializeField(gameFieldProperties, javaFxProperties, presenter);

        this.presenter.start();
        primaryStage.show();
    }

    /**
     * Executes the game.
     */
    public void execute() {
        launch();
    }
}
