package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.deliveryboy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaDeliveryBoyService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.Order;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.Address;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;
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
                 var warehouse = pizzeriaDeliveryBoyService.getWarehouse();
                 var orders = warehouse.takeMultiple(trunkCapacity);

                 orders.forEach(order -> order.setStatus(Order.Status.IN_DELIVERY));
                 orders.forEach(order -> pizzeriaDeliveryBoyService.printStatus(order));

                 for (var order : orders) {
                     TasksExecutor.INSTANCE.schedule(
                         () -> {
                             order.setStatus(Order.Status.DONE);
                             pizzeriaDeliveryBoyService.printStatus(order);
                         },
                         order.getOrderDescription()
                             .address()
                             .deliveryTimeMillis()
                     );
                 }

                 TasksExecutor.INSTANCE.schedule(
                     this::deliver,
                     orders.stream()
                         .map(Order::getOrderDescription)
                         .map(OrderDescription::address)
                         .map(Address::deliveryTimeMillis)
                         .max(Integer::compare).get()
                 );
             });
        }
}

