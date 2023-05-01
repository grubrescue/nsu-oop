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
    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/configscene.fxml"));
        Scene rootScene = fxmlLoader.load();
        primaryStage.setScene(rootScene);
        primaryStage.setTitle("SnakeGame Configuration Tool");

        Presenter presenter = fxmlLoader.getController();
        Model model = new Model();
        presenter.setModel(model);
        presenter.setModel(model);
        presenter.init();

        primaryStage.show();
    }

    /**
     * Executes the configuration tool.
     */
    public void execute() {
        launch();
    }

}
