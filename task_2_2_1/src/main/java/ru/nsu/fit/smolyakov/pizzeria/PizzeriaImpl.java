package ru.nsu.fit.smolyakov.pizzeria;

import ru.nsu.fit.smolyakov.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.entity.OrderDescription;
import ru.nsu.fit.smolyakov.pizzeria.printers.OrderStatusPrinter;
import ru.nsu.fit.smolyakov.pizzeria.printers.OrderStatusPrinterImpl;
import ru.nsu.fit.smolyakov.pizzeria.worker.Baker;
import ru.nsu.fit.smolyakov.pizzeria.worker.DeliveryBoy;
import ru.nsu.fit.smolyakov.pizzeria.worker.OrderQueue;
import ru.nsu.fit.smolyakov.pizzeria.worker.Warehouse;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PizzeriaImpl implements Pizzeria {
    private final OrderQueue orderQueue;
    private final Warehouse warehouse;
    private final List<Baker> bakerList;
    private final List<DeliveryBoy> deliveryBoyList;

    private final ExecutorService threadPool = Executors.newScheduledThreadPool(8);
    private final OrderStatusPrinter orderStatusPrinter = new OrderStatusPrinterImpl();

    private boolean working = false;
    private int orderId = 0;

    public PizzeriaImpl(OrderQueue orderQueue,
                        Warehouse warehouse,
                        List<Baker> bakerList,
                        List<DeliveryBoy> deliveryBoyList) {
        this.orderQueue = orderQueue;
        this.warehouse = warehouse;
        this.bakerList = bakerList;
        this.deliveryBoyList = deliveryBoyList;
    }

    @Override
    public void start() {
        synchronized (this) {
            working = true;
        }
    }

    @Override
    public boolean makeOrder(OrderDescription orderDescription) {
        synchronized (this) {
            while (working) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            
        }

        return true;
    }

    @Override
    public void printOrderStatus(Order order) {
        orderStatusPrinter.print(order);
    }

    @Override
    public void stop() {

    }

    //TODO
}
