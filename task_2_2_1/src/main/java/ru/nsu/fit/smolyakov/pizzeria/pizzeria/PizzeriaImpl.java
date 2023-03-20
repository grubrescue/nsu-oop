package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.OrderDescription;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.baker.Baker;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.deliveryboy.DeliveryBoy;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.orderqueue.OrderQueue;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.orderqueue.OrderQueueImpl;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.warehouse.Warehouse;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.warehouse.WarehouseImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class PizzeriaImpl implements PizzeriaOrderService,
                                     PizzeriaEmployeeService,
                                     PizzeriaOwnerService {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    private final ExecutorService threadPool = Executors.newScheduledThreadPool(8);
    private final Logger logger = Logger.getLogger("pizzeria");

    private final OrderQueue orderQueue;
    private final Warehouse warehouse;
    private final List<Baker> bakerList;
    private final List<DeliveryBoy> deliveryBoyList;

    private boolean working = false;
    private int orderId = 0;

    @JsonCreator
    private PizzeriaImpl(OrderQueue orderQueue,
                         Warehouse warehouse,
                         List<Baker> bakerList,
                         List<DeliveryBoy> deliveryBoyList) {
        this.orderQueue = orderQueue;
        this.warehouse = warehouse;
        this.bakerList = bakerList;
        this.deliveryBoyList = deliveryBoyList;
    }

    public PizzeriaImpl fromJson(InputStream stream) {
        try {
            return objectMapper.readValue(stream, PizzeriaImpl.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PizzeriaImpl fromJson(String resourceName) {
        return fromJson(this.getClass().getResourceAsStream(resourceName));
    }

    public PizzeriaImpl fromJson() {
        return fromJson("PizzeriaConfiguration.json");
    }

    @Override
    public synchronized void start() {
        working = true;
    }

    @Override
    public synchronized boolean makeOrder(OrderDescription orderDescription) {
        if (!working) {
            return false;
        } else {
            submitTask(() ->
                orderQueue.acceptOrder(Order.create(orderId++, orderDescription))
            );
            return true;
        }
    }

    @Override
    public synchronized void submitTask(Runnable task) {
        threadPool.submit(task);
    }

    @Override
    public synchronized void stop() {
        working = false;
    }
}
