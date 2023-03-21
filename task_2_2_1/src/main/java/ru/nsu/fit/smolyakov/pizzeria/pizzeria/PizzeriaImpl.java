package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.OrderDescription;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.baker.Baker;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.deliveryboy.DeliveryBoy;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue.OrderQueue;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.warehouse.Warehouse;
import ru.nsu.fit.smolyakov.pizzeria.util.PizeriaLogger;
import ru.nsu.fit.smolyakov.pizzeria.util.TasksExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class PizzeriaImpl implements PizzeriaOrderService,
                                     PizzeriaStatusPrinterService,
                                     PizzeriaOwnerService,
                                     PizzeriaBakerService,
                                     PizzeriaDeliveryBoyService {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    private final String pizzeriaName;

    private final OrderQueue orderQueue;
    private final Warehouse warehouse;
    private final List<Baker> bakerList;
    private final List<DeliveryBoy> deliveryBoyList;

    private boolean working = false;
    private int orderId = 0;

    @JsonCreator
    private PizzeriaImpl(String pizzeriaName,
                         OrderQueue orderQueue,
                         Warehouse warehouse,
                         List<Baker> bakerList,
                         List<DeliveryBoy> deliveryBoyList) {
        this.pizzeriaName = pizzeriaName;
        this.orderQueue = orderQueue;
        this.warehouse = warehouse;
        this.bakerList = bakerList;
        this.deliveryBoyList = deliveryBoyList;
    }

    public static PizzeriaImpl fromJson(InputStream stream) {
        try {
            return objectMapper.readValue(stream, PizzeriaImpl.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PizzeriaImpl fromJson(String resourceName) {
        return fromJson(PizzeriaImpl.class.getResourceAsStream(resourceName));
    }

    public static PizzeriaImpl fromJson() {
        return fromJson("PizzeriaConfiguration.json");
    }

    @Override
    public synchronized boolean makeOrder(OrderDescription orderDescription) {
        if (!working) {
            return false;
        }

        TasksExecutor.INSTANCE.execute(() ->
            orderQueue.put(Order.create(this, orderId++, orderDescription))
        );
        return true;
    }

    @Override
    public synchronized void start() {
        working = true;
    }

    @Override
    public boolean isWorking() {
        return working;
    }

    @Override
    public synchronized void stop() {
        working = false;
    }

    @Override
    public void printStatus(Order order) {
        PizeriaLogger.INSTANCE.info(order);
    }

    @Override
    public Warehouse getWarehouse() {
        return warehouse;
    }

    @Override
    public OrderQueue getOrderQueue() {
        return orderQueue;
    }

    @Override
    public String getPizzeriaName() {
        return pizzeriaName;
    }
}
