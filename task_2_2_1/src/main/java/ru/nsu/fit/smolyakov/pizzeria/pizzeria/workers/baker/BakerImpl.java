package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.baker;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaBakerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.Order;
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
    private final AtomicBoolean working = new AtomicBoolean(true);

    private BakerImpl() {
    }

    @Override
    public void cook() {
        TasksExecutor.INSTANCE.execute(
            () -> {
                if (!working.get()) {
                    return;
                }

                var orderQueue = pizzeriaBakerService.getOrderQueue();
                var warehouse = pizzeriaBakerService.getWarehouse();

                var order = orderQueue.take();

                order.setStatus(Order.Status.BEING_BAKED);
                pizzeriaBakerService.printStatus(order);

                TasksExecutor.INSTANCE.schedule(
                    () -> {
                        order.setStatus(Order.Status.WAITING_FOR_WAREHOUSE);
                        pizzeriaBakerService.printStatus(order);

                        warehouse.put(order);

                        this.cook();
                    },
                    cookingTimeMillis
                );
            }
        );
    }
}
