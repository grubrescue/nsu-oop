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
     * Allows a delivery boy to run away from work right here, right now.
     * Maybe because a pizzeria where he works is on fire. This method makes him happy.
     */
    void forceStop();

    /**
     * Informs the delivery boy that he can finish his tasks and then go home
     * and drink beer called 387. This method also makes him happy.
     */
    void stopAfterCompletion();
}
