package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.deliveryboy;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Represents a delivery boy, who can start working, stop working or
 * run away from work, as well as baker.
 */
@JsonDeserialize(as = DeliveryBoyImpl.class)
public interface DeliveryBoy {
    /**
     * Initiates delivery boy's work.
     */
    void start();

    /**
     * Informs the delivery boy that he can finish his tasks and then go home
     * and drink beer called 387. This method makes him happy.
     */
    void stop();


    /**
     * Returns if this delivery boy is working.
     * This method returns {@code false} if a worker
     * received {@link #stop()},
     * though he is allowed to finish his tasks.
     *
     * @return true if this delivery boy is working
     */
    boolean isWorking();
}
