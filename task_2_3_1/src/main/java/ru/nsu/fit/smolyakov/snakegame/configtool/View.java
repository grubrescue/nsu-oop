package ru.nsu.fit.smolyakov.snakegame.configtool;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class View {
    @FXML
    private Button saveButton;

    @FXML
    public void buttonPressed() {
        System.out.println("Button pressed");
    }
}
