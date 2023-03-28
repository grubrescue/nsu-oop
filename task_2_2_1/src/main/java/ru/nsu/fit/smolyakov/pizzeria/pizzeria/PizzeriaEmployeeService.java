package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.concurrent.Future;

/**
 * Provides functionality that can - and, to some extent, should - be used
 * by all workers.
 */
@JsonDeserialize(as = PizzeriaImpl.class)
public interface PizzeriaEmployeeService {
    /**
     * Submits a specified {@code task}. Latter will be executed
     * shortly.
     *
     * @param task a specified task
     * @return a {@link Future} instance, always returning {@code null}
     * on {@link Future#get()} method
     */
    Future<?> submit(Runnable task);

    /**
     * Schedules a specified {@code task}. Latter will be executed
     * not earlier then after {@code delayMillis}.
     *
     * @param delayMillis a specified delay in milliseconds
     * @param task        a specified task
     * @return a {@link Future} instance, always returning {@code null}
     * on {@link Future#get()} method
     */
    Future<?> schedule(int delayMillis, Runnable task);
}
