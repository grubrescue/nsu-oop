package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.OrderInformationService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;

import java.util.Optional;

/**
 * Provides functionality allowed to be used by
 * customers of a pizzeria.
 */
@JsonDeserialize(as = PizzeriaImpl.class)
public interface PizzeriaCustomerService {
    /**
     * Returns a corresponding pizzeria name.
     *
     * @return a pizzeria name
     */
    String getPizzeriaName();

    /**
     * Returns if corresponding pizzeria is working
     * and allows to create orders.
     *
     * @return {@code true} if pizzeria is working
     */
    boolean isWorking();

    /**
     * Creates an order in a corresponding pizzeria.
     *
     * @param orderDescription a description of an order
     * @return an {@link Optional} of {@link OrderInformationService},
     * that allows to track order status, {@link Optional#empty()}
     * otherwise
     */
    Optional<OrderInformationService> makeOrder(OrderDescription orderDescription);
}
