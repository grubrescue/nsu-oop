package ru.nsu.fit.smolyakov.snake.properties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;

public record GameFieldProperties(
    @JsonProperty("width") int width,
    @JsonProperty("height") int height,
    @JsonProperty("maxApples") int maxApples
){
}
