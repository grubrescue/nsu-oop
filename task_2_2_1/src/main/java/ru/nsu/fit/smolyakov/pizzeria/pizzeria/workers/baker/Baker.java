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
     * Immediately stops baker's task.
     */
    void forceStop();

    /**
     * Allows to complete current task and then leave
     * the goddamn pizzeria.
     */
    void stopAfterCompletion();

    /**
     * Returns if this baker is working.
     * This method returns {@code true} if a worker
     * received {@link #forceStop()} or {@link #stopAfterCompletion()},
     * though he is allowed to finish his tasks.
     *
     * @return true if this baker is working
     */
    boolean isWorking();
}
