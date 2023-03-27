package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.baker;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaBakerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.Order;

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
    public void start() {
        working.set(true);
        waitForOrder();
    }

    private void waitForOrder() {
        pizzeriaBakerService.execute(() -> {
            if (!working.get()) {
                return;
            }

            var orderQueue = pizzeriaBakerService.getOrderQueue();
            var order = orderQueue.take();

            order.setStatus(Order.Status.BEING_BAKED);
            cookAndStore(order);
        });
    }

    private void cookAndStore(Order order) {
        pizzeriaBakerService.schedule(
            cookingTimeMillis,
            () -> {
                order.setStatus(Order.Status.WAITING_FOR_WAREHOUSE);
                var warehouse = pizzeriaBakerService.getWarehouse();
                warehouse.put(order);
                this.waitForOrder();
            });
    }

    @Override
    public void stop() {
        working.set(false);
    }
}
