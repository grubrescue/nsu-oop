package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.concurrent.Future;

@JsonDeserialize(as = PizzeriaImpl.class)
public interface PizzeriaEmployeeService {
    Future<?> submit(Runnable task);
    Future<?> schedule(int delayMillis, Runnable task);
}
