package ru.nsu.fit.smolyakov.snakegame.configtool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class Model {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
}
