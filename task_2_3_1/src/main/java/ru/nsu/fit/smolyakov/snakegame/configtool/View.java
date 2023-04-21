package ru.nsu.fit.smolyakov.snakegame.configtool;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import ru.nsu.fit.smolyakov.snakegame.properties.GameSpeed;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class View implements Initializable {
    public static final int MIN_WIDTH = 3;
    public static final int MIN_HEIGHT = 3;
    public static final int MAX_WIDTH = 1000;
    public static final int MAX_HEIGHT = 1000;

    private Presenter presenter;

    @FXML
    private Scene scene;

    @FXML
    private ListView<String> aiListView;

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
    private ChoiceBox<String> levelChoiceBox;

    @FXML
    private Button runGameButton;

    @FXML
    public void saveConfig() {
        presenter.saveConfig();
    }

    @FXML
    public void updateCalculatedResolution() {
        presenter.onScalingChanged();
    }

    @FXML
    public void saveAndRunGame() {
        presenter.saveConfig();
        presenter.runJavaFxSnake();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void initAiNames() {
        aiListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        javaFxScalingSlider.valueProperty().addListener(presenter.onScalingChangedListener);
    }

    public void initWidthSelector(int width) {
        SpinnerValueFactory<Integer> widthSpinnerValue =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_WIDTH, MAX_WIDTH);
        widthSpinner.setEditable(true);
        widthSpinnerValue.setValue(width);
        widthSpinner.setValueFactory(widthSpinnerValue);
        widthSpinner.valueProperty().addListener(presenter.onFieldSizeChangeListener);
    }

    public void initHeightSelector(int height) {
        SpinnerValueFactory<Integer> heightSpinnerValue =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_HEIGHT, MAX_HEIGHT);
        heightSpinner.setEditable(true);
        heightSpinnerValue.setValue(height);
        heightSpinner.setValueFactory(heightSpinnerValue);
        heightSpinner.valueProperty().addListener(presenter.onFieldSizeChangeListener);
    }

    public void initApplesAmountSelector(int initValue, int maxApples) {
        SpinnerValueFactory<Integer> applesSpinnerValue =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxApples - 2);
        applesSpinner.setEditable(true);
        applesSpinnerValue.setValue(initValue);
        applesSpinner.setValueFactory(applesSpinnerValue);
    }

    public void setAvailableAiNames(List<String> aiNames) {
        aiListView.getItems().clear();
        aiListView.getItems().addAll(aiNames);
    }

    public void setAvailableLevelNames(List<String> levelNames) {
        levelChoiceBox.getItems().clear();
        levelChoiceBox.getItems().addAll(levelNames);
    }

    public void setAvailableGameSpeedChoices(List<GameSpeed> gameSpeeds) {
        speedChoiceBox.getItems().clear();
        speedChoiceBox.getItems().addAll(gameSpeeds);
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

    public void setSelectedJavaFxScalingValue(int javaFxScaling) {
        javaFxScalingSlider.setValue(javaFxScaling);
    }

    public void setResolutionText(int resX, int resY) {
        String format = "Resolution will be %d x %d px.";
        resolutionText.setText(format.formatted(resX, resY));
    }

    public int getApplesAmount() {
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

    public String getLevel() {
        return levelChoiceBox.getValue();
    }

    public void setSelectedLevel(String level) {
        levelChoiceBox.setValue(level);
    }

    public List<String> getAiNames() {
        return aiListView.getSelectionModel().getSelectedItems();
    }

    public void setSelectedAiNames(List<String> aiNames) {
        aiListView.getSelectionModel().clearSelection();
        aiNames.forEach(aiName -> {
            var index = aiListView.getItems().indexOf(aiName);
            aiListView.getSelectionModel().select(aiName);
        });
    }

    public void setApplesAvailableRange(int upon) {
        var svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, upon);
        applesSpinner.setValueFactory(svf);
    }

    public Scene getScene() {
        return scene;
    }
}
