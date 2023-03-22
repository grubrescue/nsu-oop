package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.deliveryboy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaDeliveryBoyService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.util.TasksExecutor;

import java.beans.ConstructorProperties;

public class DeliveryBoyImpl implements DeliveryBoy {
    @JsonBackReference
    private PizzeriaDeliveryBoyService pizzeriaDeliveryBoyService;
    private final int trunkCapacity;

    private final int id;

    @JsonCreator
    @ConstructorProperties({"id", "trunkCapacity"})
    public DeliveryBoyImpl(int id, int trunkCapacity) {
        this.id = id;
        this.trunkCapacity = trunkCapacity;
    }

    @Override
    public void deliver() {
        TasksExecutor.INSTANCE.execute(
            () -> {
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
                    }
                    );
            }
        );
    }
}
