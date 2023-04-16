package ru.nsu.fit.smolyakov.snake.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.nsu.fit.smolyakov.snake.model.Apple;
import ru.nsu.fit.smolyakov.snake.model.Barrier;
import ru.nsu.fit.smolyakov.snake.model.Point;
import ru.nsu.fit.smolyakov.snake.model.Snake;
import ru.nsu.fit.smolyakov.snake.presenter.Presenter;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;

public class JavaFxView implements View, Initializable {
    private Presenter presenter;

    private Stage stage;
    private JavaFxContext context;
    private int proportion;

    @FXML
    private Canvas canvas;

    @FXML
    private Text scoreAmountText;

    // resources
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

    public void createField(Stage stage, Scene scene, JavaFxContext context, Presenter presenter) {
        if (stage == null || context == null || presenter == null) {
            throw new IllegalArgumentException("Stage or context is null");
        }

        if (context.resX() % (2 * context.gameFieldWidth()) != 0
            || context.resY() % (2 * context.gameFieldHeight()) != 0) {
            throw new IllegalArgumentException("Resolution is not divisible by game field size");
        }

        if (context.resX() / context.gameFieldWidth() !=
            context.resY() / context.gameFieldHeight()) {
            throw new IllegalArgumentException("Resolution is not proportional to game field size");
        }

        this.proportion = context.resX() / context.gameFieldWidth();

        this.scene = scene;
        this.stage = stage;
        this.context = context;

        this.presenter = presenter;

        this.stage.setWidth(context.resX());
        this.stage.setHeight(context.resY());
        this.stage.setTitle(context.title());

        this.scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP -> presenter.onUpKeyPressed();
                case DOWN -> presenter.onDownKeyPressed();
                case LEFT -> presenter.onLeftKeyPressed();
                case RIGHT -> presenter.onRightKeyPressed();
            }
        });

        canvas.setWidth(context.resX());
        canvas.setHeight(context.resY());
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
        canvas.getGraphicsContext2D().clearRect(0, 0, context.resX(), context.resY());
    }
}
