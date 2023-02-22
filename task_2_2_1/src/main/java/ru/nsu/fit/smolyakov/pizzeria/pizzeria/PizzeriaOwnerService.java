package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Provides functionality allowed to be used
 */
@JsonDeserialize(as = PizzeriaImpl.class)
public interface PizzeriaOwnerService {
    /**
     * Starts a corresponding pizzeria.
     */
    void start();

    /**
     * Returns if corresponding pizzeria is working
     * and allows to create orders.
     *
     * @return {@code true} if pizzeria is working
     */
    boolean isWorking();

    /**
     * Stops a corresponding pizzeria. That means pizzeria won't accept new orders,
     * but already created ones will be completed.
     */
    void stop();

    /**
     * Immediately stops a corresponding pizzeria. All created orders are treated as if they
     * have never existed.
     */
    void forceStop();

    /**
     * Returns an {@link PizzeriaCustomerService} interface designed for consumers.
     *
     * @return an {@link PizzeriaCustomerService} interface designed for consumers.
     */
    PizzeriaCustomerService getOrderService();
}
