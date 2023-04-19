package ru.nsu.fit.smolyakov.snakegame.configtool;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
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
    private Spinner<Integer> widthSpinner;

    @FXML
    private Spinner<Integer> heightSpinner;

    @FXML
    private Spinner<Integer> applesSpinner;

    @FXML
    private Slider javaFxScalingSlider;

    @FXML
    private Text resolutionText;

    @FXML
    private ChoiceBox<String> barrierChoiceBox;

    @FXML
    public void saveConfig() {
        presenter.saveConfig();
    }

    @FXML
    public void updateCalculatedResolution() {
        presenter.scalingChanged();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ChangeListener<Number> scalingListener = (observable, oldValue, newValue) -> {
            if (presenter != null) {
                presenter.scalingChanged();
            }
        };

        ChangeListener<Number> applesListener = (observable, oldValue, newValue) -> {
            var newMax = getWidth() * getHeight() - 2;
            var prevVal = applesSpinner.getValue();
            var svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, newMax);
            svf.setValue(Integer.min(prevVal, newMax));
            applesSpinner.setValueFactory(svf);
        };

        speedChoiceBox.getItems().addAll(GameSpeed.values());

        javaFxScalingSlider.valueProperty().addListener(scalingListener);

        SpinnerValueFactory<Integer> widthSpinnerValue =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 1000);
        widthSpinner.setEditable(true);
        widthSpinner.setValueFactory(widthSpinnerValue);
        widthSpinner.valueProperty().addListener(scalingListener);
        widthSpinner.valueProperty().addListener(applesListener);

        SpinnerValueFactory<Integer> heightSpinnerValue =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 1000);
        heightSpinner.setEditable(true);
        heightSpinner.setValueFactory(heightSpinnerValue);
        heightSpinner.valueProperty().addListener(scalingListener);
        heightSpinner.valueProperty().addListener(applesListener);

        SpinnerValueFactory<Integer> applesSpinnerValue =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, getWidth() * getHeight() - 2);
        applesSpinner.setEditable(true);
        applesSpinner.setValueFactory(applesSpinnerValue);
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public GameSpeed getGameSpeed() {
        return speedChoiceBox.getValue();
    }

    public void setGameSpeed(GameSpeed gameSpeed) {
        speedChoiceBox.setValue(gameSpeed);
    }

    public void setWidth(int width) {
        widthSpinner.getValueFactory().setValue(width);
    }

    public void setHeight(int height) {
        heightSpinner.getValueFactory().setValue(height);
    }

    public void setApples(int apples) {
        applesSpinner.getValueFactory().setValue(apples);
    }

    public void setJavaFxScalingSlider(int javaFxScaling) {
        javaFxScalingSlider.setValue(javaFxScaling);
    }

    public void setResolutionText(int resX, int resY) {
        String format = "Resolution will be %d x %d px.";
        resolutionText.setText(format.formatted(resX, resY));
    }

    public int getApples() {
        return applesSpinner.getValue();
    }

    public int getWidth() {
        return widthSpinner.getValue();
    }

    public int getHeight() {
        return heightSpinner.getValue();
    }

    public int getJavaFxScalingSlider() {
        return (int) javaFxScalingSlider.getValue();
    }

    public String getBarrier() {
        return barrierChoiceBox.getValue();
    }

    public void setBarrier(String barrier) {
        barrierChoiceBox.setValue(barrier);
    }
}
