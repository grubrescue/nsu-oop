package ru.nsu.fit.smolyakov.snake.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.nsu.fit.smolyakov.snake.model.Apple;
import ru.nsu.fit.smolyakov.snake.model.Barrier;
import ru.nsu.fit.smolyakov.snake.model.Point;
import ru.nsu.fit.smolyakov.snake.model.Snake;
import ru.nsu.fit.smolyakov.snake.presenter.Presenter;
import ru.nsu.fit.smolyakov.snake.properties.GameFieldProperties;
import ru.nsu.fit.smolyakov.snake.properties.JavaFxProperties;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;

public class JavaFxView implements View, Initializable {
    private Presenter presenter;

    private Stage stage;
    private JavaFxProperties javaFxProperties;
    private int proportion;

    @FXML
    private Canvas canvas;

    @FXML
    private Text scoreAmountText;

    // resources
    // TODO вынести отдельно и доделать других змеек
    private Image appleImage;
    private Image barrierImage;
    private Image snakeTailImage;
    private Image snakeHeadImage;
    private Scene scene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private Image imageInstance(String path) {
        return new Image(
            Objects.requireNonNull(getClass().getResourceAsStream(path)),
            proportion,
            proportion,
            true,
            true
        );
    }

    public void createField(Stage stage,
                            Scene scene,
                            GameFieldProperties properties,
                            JavaFxProperties javaFxProperties,
                            Presenter presenter) {
        if (stage == null || javaFxProperties == null || presenter == null) {
            throw new IllegalArgumentException("Stage or javaFxProperties is null");
        }

        if (javaFxProperties.resX() % (2 * properties.width()) != 0
            || javaFxProperties.resY() % (2 * properties.height()) != 0) {
            throw new IllegalArgumentException("Resolution is not divisible by game field size");
        }

        if (javaFxProperties.resX() / properties.width() !=
            javaFxProperties.resY() / properties.height()) {
            throw new IllegalArgumentException("Resolution is not proportional to game field size");
        }

        this.proportion = javaFxProperties.resX() / properties.width();

        this.scene = scene;
        this.stage = stage;
        this.javaFxProperties = javaFxProperties;

        this.presenter = presenter;

        this.stage.setWidth(javaFxProperties.resX());
        this.stage.setHeight(javaFxProperties.resY());
        this.stage.setTitle(javaFxProperties.title());

        this.scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP -> presenter.onUpKeyPressed();
                case DOWN -> presenter.onDownKeyPressed();
                case LEFT -> presenter.onLeftKeyPressed();
                case RIGHT -> presenter.onRightKeyPressed();
                case Q -> presenter.onExitKeyPressed();
                case R -> presenter.onRestartKeyPressed();
            }
        });

        canvas.setWidth(javaFxProperties.resX());
        canvas.setHeight(javaFxProperties.resY());
        // TODO разделить как то чтоли


        appleImage = imageInstance("/apple.png");
//        barrierImage = imageInstance("/barrier.png");
        snakeTailImage = imageInstance("/tail.png");
        snakeHeadImage = imageInstance("/head.png");
    }

    private void drawFigure(Point point, Image image) {
        var graphicsContext = canvas.getGraphicsContext2D(); // TODO надо ли хранить в поле или получать каждый раз???

        graphicsContext.drawImage(
            image,
            point.x() * proportion,
            point.y() * proportion,
            proportion,
            proportion
        );
    }

    public void setScoreAmount(int scoreAmount) {
        this.scoreAmountText.setText(String.valueOf(scoreAmount));
    }

    public void draw(Set<Apple> appleSet) {
        appleSet.forEach(apple -> drawFigure(apple.point(), appleImage));
    }

    public void draw(Barrier barrier) {
        barrier.barrierPoints().forEach(point -> drawFigure(point, barrierImage));
    }

    public void draw(Snake snake) {
        drawFigure(snake.getSnakeBody().getHead(), snakeHeadImage); // TODO сделать поворот
        snake.getSnakeBody().getTail().forEach(point -> drawFigure(point, snakeTailImage));
    }

    public void clear() {
        canvas.getGraphicsContext2D().clearRect(0, 0, javaFxProperties.resX(), javaFxProperties.resY());
    }

    public void showMessage(String message) {
        canvas.getGraphicsContext2D().strokeText(message, javaFxProperties.resX() / 3, javaFxProperties.resY() / 3);
    }

    public void close() {
        stage.close();
    }
}
