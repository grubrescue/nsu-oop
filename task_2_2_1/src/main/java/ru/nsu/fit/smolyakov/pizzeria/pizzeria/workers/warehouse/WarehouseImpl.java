package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.warehouse;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaStatusPrinterService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.util.ConsumerProducerQueue;

import java.util.List;

public class WarehouseImpl implements Warehouse {
    @JsonManagedReference
    private PizzeriaStatusPrinterService pizzeriaStatusPrinterService;

    private final ConsumerProducerQueue<Order> consumerProducerQueue;

    public WarehouseImpl(int capacity) {
        this.consumerProducerQueue = new ConsumerProducerQueue<>(capacity);
    }


    @Override
    public void put(Order order) {
        try {
            consumerProducerQueue.put(order);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> takeMultiple(int maxAmount) {
        return null;
    }
}
