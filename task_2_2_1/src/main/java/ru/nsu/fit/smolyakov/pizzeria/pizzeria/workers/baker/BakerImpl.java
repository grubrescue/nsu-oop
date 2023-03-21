package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.baker;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaBakerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.util.TasksExecutor;

public class BakerImpl implements Baker {
    @JsonManagedReference
    private PizzeriaBakerService pizzeriaBakerService;
    private final int cookingTimeMillis;

    public BakerImpl(int cookingTimeMillis) {
        this.cookingTimeMillis = cookingTimeMillis;
    }

    @Override
    public void cook() {
        TasksExecutor.INSTANCE.execute(
            () -> {
                var orderQueue = pizzeriaBakerService.getOrderQueue();

                var order = orderQueue.take();

                order.setStatus(Order.Status.BEING_BAKED);
                pizzeriaBakerService.printStatus(order);

                try {
                    Thread.sleep(cookingTimeMillis);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                var warehouse = pizzeriaBakerService.getWarehouse();
                warehouse.put(order);

                order.setStatus(Order.Status.WAITING_FOR_DELIVERY);
                pizzeriaBakerService.printStatus(order);
            }
        );
    }
}
