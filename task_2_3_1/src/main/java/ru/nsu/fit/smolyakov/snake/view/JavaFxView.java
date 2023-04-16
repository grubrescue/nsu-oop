package ru.nsu.fit.smolyakov.snake.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import ru.nsu.fit.smolyakov.snake.model.Apple;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;

public class JavaFxView implements Initializable {
    @FXML
    private Canvas canvas;

    @FXML
    private Text scoreAmountText;

    private GraphicsContext graphicsContext;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void applyProperties(JavaFxProperties properties) { // TODO переименовать или сделать чтобы норм было все
        canvas.setWidth(properties.width());
        canvas.setHeight(properties.height());
    }


    private void drawFigure() {
    }

    public void setScoreAmount(int scoreAmount) {
        this.scoreAmountText.setText(String.valueOf(scoreAmountText));
    }

    public void drawApples(Set<Apple> appleSet) {

    }


}
