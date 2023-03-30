package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.baker;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * A representation of a baker, who can start working, stop working or
 * run away from work.
 */
@JsonDeserialize(as = BakerImpl.class)
public interface Baker {
    /**
     * Initiates baker's work.
     */
    void start();

    /**
     * Allows to complete current task and then leave
     * the goddamn pizzeria.
     */
    void stop();

    /**
     * Returns if this baker is working.
     * This method returns {@code false} if a worker
     * received {@link #stop()},
     * though he is allowed to finish his tasks.
     *
     * @return {@code true} if this baker is working
     */
    boolean isWorking();
}
