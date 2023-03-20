package ru.nsu.fit.smolyakov.pizzeria.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum TaskExecutor {
    INSTANCE;

    private final ExecutorService threadPool =
        Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    public void execute(Runnable task) {
        threadPool.execute(task);
    }
}
