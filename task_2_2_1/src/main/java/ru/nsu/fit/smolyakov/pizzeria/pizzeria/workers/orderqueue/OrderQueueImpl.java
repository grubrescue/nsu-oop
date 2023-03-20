package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaEmployeeService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;

public class OrderQueueImpl implements OrderQueue {
    private boolean working = false;

    @JsonManagedReference
    private PizzeriaEmployeeService pizzeriaEmployeeService;

    @Override
    public synchronized boolean acceptOrder(Order order) {
        if (!working) {
            return false;
        } else {
            pizzeriaEmployeeService.submitTask(

            );
            return true;
        }
    }

    @Override
    public synchronized Order giveOrderToBaker() {
        return null;
    }

    @Override
    public void start() {
        working = true;
    }

    @Override
    public void stop() {
        working = false;
    }
}
