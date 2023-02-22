package ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaCustomerService;

/**
 * Represents information that is allowed to be seen by the customer
 * after ordering a pizza.
 */
public interface OrderInformationService {
    /**
     * Returns an interface that previously was used to make this order.
     * If customer likes a pizza, he can order one more.
     *
     * @return a {@link PizzeriaCustomerService}
     */
    PizzeriaCustomerService getPizzeriaCustomerService();

    /**
     * Returns current {@link Order.Status} of this order.
     *
     * @return current status
     */
    Order.Status getStatus();

    /**
     * Returns an identifier of this
     * order given by pizzeria.
     *
     * @return an identifier of this order
     */
    int getId();
}
