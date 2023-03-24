package ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOrderService;

/**
 * Represents information that is allowed to be seen by the customer
 * after ordering a pizza.
 */
public interface OrderInformationService {
    /**
     * Returns an interface that previously was used to make this order.
     * If customer likes a pizza, he can order one more.
     *
     * @return a {@link PizzeriaOrderService}
     */
    PizzeriaOrderService getPizzeriaOrderService();

    /**
     * Returns current status of this order.
     *
     * @return current status of this order
     */
    Order.Status getStatus();

    /**
     * Returns an identificator of this
     * order given by pizzeria.
     *
     * @return an identificator of this order
     */
    int getId();
}
