package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue.OrderQueue;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.warehouse.Warehouse;

@JsonDeserialize(as = PizzeriaImpl.class)
public interface PizzeriaBakerService extends PizzeriaStatusPrinterService {
    Warehouse getWarehouse();

    OrderQueue getOrderQueue();
}
