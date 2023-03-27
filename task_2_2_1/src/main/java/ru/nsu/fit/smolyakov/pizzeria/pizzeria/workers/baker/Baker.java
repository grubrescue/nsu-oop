package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.baker;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = BakerImpl.class)
public interface Baker {
    void start();
    void stop();
    void stopAfterCompletion();
}
