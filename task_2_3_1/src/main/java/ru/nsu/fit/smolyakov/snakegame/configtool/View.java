package ru.nsu.fit.smolyakov.snakegame.configtool;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import ru.nsu.fit.smolyakov.snakegame.properties.GameSpeed;

import java.net.URL;
import java.util.ResourceBundle;

public class View implements Initializable {
    private Presenter presenter;

    @FXML
    private Button saveButton;

    @FXML
    private ChoiceBox<GameSpeed> speedChoiceBox;

    @FXML
    public void saveConfig() {
        presenter.saveConfig();
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        speedChoiceBox.getItems().addAll(GameSpeed.values());
    }

    public GameSpeed getGameSpeed() {
        return speedChoiceBox.getValue();
    }

    public void setGameSpeed(GameSpeed gameSpeed) {
        speedChoiceBox.setValue(gameSpeed);
    }
}
