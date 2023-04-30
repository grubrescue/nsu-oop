package ru.nsu.fit.smolyakov.snakegame.configtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The entry point of the configuration tool.
 */
public class JavaFxConfigTool extends Application {
    private Model model;
    private Presenter presenter;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/configscene.fxml"));
        Scene rootScene = fxmlLoader.load();
        primaryStage.setScene(rootScene);
        primaryStage.setTitle("SnakeGame Configuration Tool");

        this.presenter = fxmlLoader.getController();
        this.model = new Model();
        this.presenter.setModel(model);
        this.presenter.setModel(model);
        this.presenter.init();

        primaryStage.show();
    }

    /**
     * Executes the configuration tool.
     */
    public void execute() {
        launch();
    }

}
