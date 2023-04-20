package ru.nsu.fit.smolyakov.snakegame.configtool;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import ru.nsu.fit.smolyakov.snakegame.Application;
import ru.nsu.fit.smolyakov.snakegame.properties.GameSpeed;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class View implements Initializable {
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
    private ChoiceBox<String> barrierChoiceBox;

    @FXML
    private Button runGameButton;

    @FXML
    public void saveConfig() {
        presenter.saveConfig();
    }

    @FXML
    public void updateCalculatedResolution() {
        presenter.scalingChanged();
    }

    @FXML
    public void runGame() {
        presenter.runJavaFxSnake();
    }

    private List<String> aiNames(String path) {
        try (InputStream stream = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream(path.replaceAll("[.]", "/"))) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(stream)));
            return reader.lines().map(str -> str.substring(0, str.length() - 6)).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO refactor this
        // TODO и вообще всю хрень где определяются списочки надо в презентер

        var aiNames = aiNames(Application.AI_SNAKES_PACKAGE_NAME);
        aiListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        aiListView.getItems().addAll(aiNames);



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
        try (var barrierPathsList = Files.list(Paths.get(Application.LEVEL_FOLDER_PATH))){
            var barrierFilenamesList = barrierPathsList
                .map(Path::getFileName)
                .map(Path::toString)
                .toList();
            barrierChoiceBox.getItems().addAll(barrierFilenamesList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    public List<String> getAiNames() {
        return aiListView.getSelectionModel().getSelectedItems();
    }

    public void setAiNames(List<String> aiNames) {
        aiListView.getSelectionModel().clearSelection();
        aiNames.forEach(aiName -> {
            var index = aiListView.getItems().indexOf(aiName);
            aiListView.getSelectionModel().select(aiName);
        });
    }

    public Scene getScene() {
        return scene;
    }
}
