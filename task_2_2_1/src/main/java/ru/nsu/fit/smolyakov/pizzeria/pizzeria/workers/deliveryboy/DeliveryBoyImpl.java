package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.deliveryboy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaDeliveryBoyService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.Order;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.Address;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;

import java.util.Queue;
import java.util.concurrent.Future;
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

    @JsonIgnore
    private Future<?> currentTaskFuture;

    private DeliveryBoyImpl() {}

    @Override
    public void start() {
        working.set(true);
        waitForOrders();
    }

    private void waitForOrders() {
         currentTaskFuture = pizzeriaDeliveryBoyService.submit(() -> {
             if (!working.get()) {
                 return;
             }

             var warehouse = pizzeriaDeliveryBoyService.getWarehouse();
             var orders = warehouse.takeMultiple(trunkCapacity);

             orders.forEach(order -> order.setStatus(Order.Status.IN_DELIVERY));

             deliverOrders(orders);
         });
    }

    private void deliverOrders(Queue<Order> orders) {
        for (var order : orders) {
            pizzeriaDeliveryBoyService.schedule(
                order.getOrderDescription()
                    .address()
                    .deliveryTimeMillis(),
                () -> order.setStatus(Order.Status.DONE)
            );
        }

        var timeToReachMostRemoteDestinationMillis =
            orders.stream()
                .map(Order::getOrderDescription)
                .map(OrderDescription::address)
                .map(Address::deliveryTimeMillis)
                .max(Integer::compare).get();
        comeBack(2 * timeToReachMostRemoteDestinationMillis);
    }

    private void comeBack(int afterMillis) {
        currentTaskFuture = pizzeriaDeliveryBoyService.schedule(
            afterMillis,
            this::waitForOrders
        );
    }

    @Override
    public void stop() {
        working.set(false);
    }

    @Override
    public void stopAfterCompletion() {

    }
}

