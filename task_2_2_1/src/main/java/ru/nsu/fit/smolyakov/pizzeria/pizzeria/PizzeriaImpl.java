package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.OrderDescription;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.baker.Baker;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.deliveryboy.DeliveryBoy;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue.OrderQueue;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.warehouse.Warehouse;
import ru.nsu.fit.smolyakov.pizzeria.util.PizzeriaPrinter;
import ru.nsu.fit.smolyakov.pizzeria.util.TasksExecutor;

import java.beans.ConstructorProperties;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class PizzeriaImpl implements PizzeriaOrderService,
                                     PizzeriaStatusPrinterService,
                                     PizzeriaOwnerService,
                                     PizzeriaBakerService,
                                     PizzeriaDeliveryBoyService {
    private final static ObjectMapper mapper = new ObjectMapper();
    private final static PizzeriaPrinter pizzeriaPrinter = new PizzeriaPrinter(System.out);

    @JsonProperty("name")
    private String pizzeriaName;

    @JsonManagedReference(value = "orderQueue")
    private OrderQueue orderQueue;

    @JsonManagedReference(value = "warehouse")
    private Warehouse warehouse;

    @JsonProperty("bakers")
    @JsonManagedReference(value = "bakers")
    private List<Baker> bakerList;

    @JsonProperty("deliveryBoys")
    @JsonManagedReference(value = "deliveryBoys")
    private List<DeliveryBoy> deliveryBoyList;

    @JsonIgnore
    private boolean working = false;
    @JsonIgnore
    private int orderId = 0;

    private PizzeriaImpl() {};

    public static PizzeriaImpl fromJson(InputStream stream) {
        try {
            return mapper.readValue(stream, PizzeriaImpl.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized boolean makeOrder(OrderDescription orderDescription) {
        if (!working) {
            return false;
        }

        orderQueue.put(Order.create(this, orderId++, orderDescription));
        System.out.println("gottagrip");
        return true;
    }

    @Override
    public synchronized void start() {
        working = true;

        orderQueue.start();
        bakerList.forEach(Baker::cook);
        deliveryBoyList.forEach(DeliveryBoy::deliver);
    }

    @Override
    public boolean isWorking() {
        return working;
    }

    @Override
    public synchronized void stop() {
        orderQueue.stop();
//        bakerList.forEach(Baker::stop);
//        deliveryBoyList.forEach(DeliveryBoy::deliver);
        working = false;
    }

    @Override
    public PizzeriaOrderService getOrderService() {
        return this;
    }

    @Override
    public void printStatus(Order order) {
        pizzeriaPrinter.print(order);
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
