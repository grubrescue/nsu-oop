package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.baker;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaBakerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.util.TasksExecutor;

import java.util.concurrent.atomic.AtomicBoolean;

public class BakerImpl implements Baker {
    @JsonBackReference(value = "bakers")
    private PizzeriaBakerService pizzeriaBakerService;

    @JsonProperty("cookingTimeMillis")
    private int cookingTimeMillis;

    @JsonProperty("id")
    private int id;

    @JsonIgnore
    private final AtomicBoolean working = new AtomicBoolean(false);

    private BakerImpl() {
    }

    @Override
    public void cook() {
        working.set(true);

        TasksExecutor.INSTANCE.execute(
            () -> {
                var orderQueue = pizzeriaBakerService.getOrderQueue();
                while (working.get()) {
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
            }
        );
    }
}
