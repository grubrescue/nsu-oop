package ru.nsu.fit.smolyakov.pizzeria.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public enum TasksExecutor {
    INSTANCE;

    private final ScheduledExecutorService threadPool =
        Executors.newScheduledThreadPool(/*Runtime.getRuntime().availableProcessors()*/1000);

    public void execute(Runnable task) {
        threadPool.execute(task);
    }

    public void schedule(Runnable task, int delayMillis) {
        threadPool.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
    }
}
