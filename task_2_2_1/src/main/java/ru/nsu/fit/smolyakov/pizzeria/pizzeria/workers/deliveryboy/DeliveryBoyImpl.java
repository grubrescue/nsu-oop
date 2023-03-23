package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.deliveryboy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaDeliveryBoyService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.util.TasksExecutor;

import java.util.concurrent.atomic.AtomicBoolean;

public class DeliveryBoyImpl implements DeliveryBoy {
    @JsonBackReference(value = "deliveryBoys")
    private PizzeriaDeliveryBoyService pizzeriaDeliveryBoyService;

    @JsonProperty("trunkCapacity")
    private int trunkCapacity;

    @JsonProperty("id")
    private int id;

    @JsonIgnore
    private final AtomicBoolean working = new AtomicBoolean(false);

    private DeliveryBoyImpl() {
    }

    @Override
    public void deliver() {
        TasksExecutor.INSTANCE.execute(
            () -> {
                while (working.get()) {
                    var warehouse = pizzeriaDeliveryBoyService.getWarehouse();

                    var orderQueue = warehouse.takeMultiple(trunkCapacity);

                    orderQueue.forEach(order -> order.setStatus(Order.Status.IN_DELIVERY));
                    orderQueue.forEach(order -> pizzeriaDeliveryBoyService.printStatus(order));

                    orderQueue.forEach(
                        order -> {
                            try {
                                Thread.sleep(order.getOrderDescription().address().deliveryTime());
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            order.setStatus(Order.Status.DONE);
                            pizzeriaDeliveryBoyService.printStatus(order);
                        });
                }
            }
        );
    }
}
