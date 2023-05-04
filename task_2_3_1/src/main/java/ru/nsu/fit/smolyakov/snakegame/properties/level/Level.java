package ru.nsu.fit.smolyakov.snakegame.properties.level;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.nsu.fit.smolyakov.snakegame.properties.GameProperties;
import ru.nsu.fit.smolyakov.snakegame.utils.Point;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(
    {
        @JsonSubTypes.Type(value = BorderLevel.class, name = "Border"),
        @JsonSubTypes.Type(value = CustomFileLevel.class, name = "CustomFile"),
        @JsonSubTypes.Type(value = EmptyLevel.class, name = "Empty"),
        @JsonSubTypes.Type(value = RandomLevel.class, name = "Random")
    }
)
public abstract sealed class Level permits BorderLevel, CustomFileLevel, EmptyLevel, RandomLevel {
}