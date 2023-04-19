package ru.nsu.fit.smolyakov.snakegame.configtool;

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
        speedChoiceBox.getItems().addAll(GameSpeed.values());

        SpinnerValueFactory<Integer> widthSpinnerValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000);
        widthSpinner.setEditable(true);
        widthSpinner.setValueFactory(widthSpinnerValue);

        SpinnerValueFactory<Integer> heightSpinnerValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000);
        heightSpinner.setEditable(true);
        heightSpinner.setValueFactory(heightSpinnerValue);

        SpinnerValueFactory<Integer> applesSpinnerValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);
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
