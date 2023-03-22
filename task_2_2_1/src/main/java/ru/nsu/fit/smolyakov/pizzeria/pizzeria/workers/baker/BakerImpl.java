package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.baker;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaBakerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.util.TasksExecutor;

import java.beans.ConstructorProperties;

public class BakerImpl implements Baker {
    @JsonBackReference(value = "bakers")
    private PizzeriaBakerService pizzeriaBakerService;

    @JsonProperty("cookingTimeMillis")
    private int cookingTimeMillis;

    @JsonProperty("id")
    private int id;

    private BakerImpl() {};

    @Override
    public void cook() {
        TasksExecutor.INSTANCE.execute(
            () -> {
                while (true) {//TODO NOT TRUE
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
            }
        );
    }
}
