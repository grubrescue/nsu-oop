package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    private final String pizzeriaName;

    @JsonManagedReference
    private OrderQueue orderQueue;
    @JsonManagedReference
    private Warehouse warehouse;
    @JsonManagedReference
    private List<Baker> bakerList;
    @JsonManagedReference
    private List<DeliveryBoy> deliveryBoyList;

    private boolean working = false;
    private int orderId = 0;

    @ConstructorProperties({"name", "orderQueue", "warehouse", "bakers", "deliveryBoys"})
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
        return true;
    }

    @Override
    public synchronized void start() {
        working = true;

        orderQueue.start();
        bakerList.forEach(baker -> TasksExecutor.INSTANCE.execute(baker::cook));
        deliveryBoyList.forEach(deliveryBoy -> TasksExecutor.INSTANCE.execute(deliveryBoy::deliver));
    }

    @Override
    public boolean isWorking() {
        return working;
    }

    @Override
    public synchronized void stop() {
        orderQueue.stop();
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
