package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.warehouse;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaStatusPrinterService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.util.ConsumerProducerQueue;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Queue;

public class WarehouseImpl implements Warehouse {
    @JsonBackReference(value = "warehouse")
    private PizzeriaStatusPrinterService pizzeriaStatusPrinterService;

    @JsonProperty("capacity")
    private int capacity;

    @JsonIgnore
    private final ConsumerProducerQueue<Order> consumerProducerQueue
        = new ConsumerProducerQueue<>(capacity);;

    private WarehouseImpl() {};

    @Override
    public void put(Order order) {
        try {
            consumerProducerQueue.put(order);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Queue<Order> takeMultiple(int maxAmount) {
        try {
            return consumerProducerQueue.takeMultiple(maxAmount);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
