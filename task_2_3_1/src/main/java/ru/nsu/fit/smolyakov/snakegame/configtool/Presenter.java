package ru.nsu.fit.smolyakov.snakegame.configtool;

import java.io.IOException;

public class Presenter {
    private Model model;
    private View view;

    public Presenter(Model model, View view) {
        this.model = model;
        this.view = view;

        view.setPresenter(this);

        view.setGameSpeed(model.getProperties().speed());
        view.setWidth(model.getProperties().width());
        view.setHeight(model.getProperties().height());
        view.setApples(model.getProperties().apples());
        view.setJavaFxScalingSlider(model.getProperties().javaFxScaling());
        view.updateCalculatedResolution();
        view.setBarrier(model.getProperties().barrierFileName());
        view.setAiNames(model.getProperties().aiClassNamesList());
    }

    public void saveConfig() {
        model.setProperties(
            model.getProperties()
                .withSpeed(view.getGameSpeed())
                .withApples(view.getApples())
                .withWidth(view.getWidth())
                .withHeight(view.getHeight())
                .withJavaFxScaling(view.getJavaFxScalingSlider())
                .withBarrierFilePath(view.getBarrier())
                .withAiClassNamesList(view.getAiNames())
        );

        try {
            model.sync();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void scalingChanged() {
        var scaling = view.getJavaFxScalingSlider();
        view.setResolutionText(
            scaling * view.getWidth(),
            scaling * view.getHeight()
        );
    }
}
