package ru.nsu.fit.smolyakov.pizzeria;

import ru.nsu.fit.smolyakov.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.worker.Baker;
import ru.nsu.fit.smolyakov.pizzeria.worker.DeliveryBoy;
import ru.nsu.fit.smolyakov.pizzeria.worker.OrderQueue;
import ru.nsu.fit.smolyakov.pizzeria.worker.Warehouse;

import java.util.List;
public class PizzeriaImpl implements Pizzeria {
    private OrderQueue orderQueue;
    private Warehouse warehouse;
    private List<Baker> bakerList;
    private List<DeliveryBoy> deliveryBoyList;

    @Override
    public void start() {

    }

    @Override
    public void makeOrder(Order order) {

    }

    //TODO
}
