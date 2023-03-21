package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.deliveryboy;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaDeliveryBoyService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;
import ru.nsu.fit.smolyakov.pizzeria.util.TasksExecutor;

public class DeliveryBoyImpl implements DeliveryBoy {
    @JsonManagedReference
    private PizzeriaDeliveryBoyService pizzeriaDeliveryBoyService;
    private final int trunkCapacity;

    public DeliveryBoyImpl(int trunkCapacity) {
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
