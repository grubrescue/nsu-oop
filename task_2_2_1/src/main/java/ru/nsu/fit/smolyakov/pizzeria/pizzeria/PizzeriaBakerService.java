package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue.OrderQueue;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.warehouse.Warehouse;

/**
 * Provides functionality allowed to be used by
 * {@link ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.baker.Baker}.
 */
@JsonDeserialize(as = PizzeriaImpl.class)
public interface PizzeriaBakerService extends PizzeriaEmployeeService {
    /**
     * Returns a warehouse.
     * @return a warehouse.
     */
    Warehouse getWarehouse();

    /**
     * Returns an order queue.
     * @return an order queue.
     */
    OrderQueue getOrderQueue();
}
