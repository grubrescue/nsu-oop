package ru.nsu.fit.smolyakov.snakegame.configtool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.nsu.fit.smolyakov.snakegame.model.GameFieldImpl;
import ru.nsu.fit.smolyakov.snakegame.presenter.SnakePresenter;
import ru.nsu.fit.smolyakov.snakegame.properties.GameFieldProperties;
import ru.nsu.fit.smolyakov.snakegame.properties.JavaFxProperties;
import ru.nsu.fit.smolyakov.snakegame.properties.PresenterProperties;

import java.io.IOException;
import java.util.Objects;

public class JavaFxConfigTool extends Application {
    private View view;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        var mapper = new ObjectMapper(new YAMLFactory());

        var fxmlLoader = new FXMLLoader(getClass().getResource("/configscene.fxml"));
        Scene rootScene = fxmlLoader.load();

        primaryStage.setScene(rootScene);

        this.view = fxmlLoader.getController();
//        this.model = new GameFieldImpl(gameFieldProperties);
//        this.snakePresenter = new SnakePresenter(this.view, this.model, presenterProperties);
//
//        this.view.initializeField(gameFieldProperties, javaFxProperties, snakePresenter);
//
//        this.snakePresenter.start();
        primaryStage.show();
    }

    /**
     * Executes the configuration tool.
     */
    public void execute() {
        launch();
    }

}
