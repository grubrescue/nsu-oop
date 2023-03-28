package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.deliveryboy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaDeliveryBoyService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.Order;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.Address;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.baker.Baker;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue.OrderQueue;

import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An implementation of {@link DeliveryBoy} interface.
 *
 * <p>Behaves as if a delivery boy was a finite automata - firstly, he waits for
 * an {@link Order} in a {@link ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.warehouse.Warehouse},
 * or multiple ones, then he delivers it, or them, to the destination address.
 *
 * <p>In order to simplify the difficult task of the delivery boy, imagine that the
 * Earth is flat and one-dimensional. So that, we can represent one on the coordinate line.
 *
 * <blockquote><pre>
 * <br/>(PIZZERIA)---(100ms)--->(CUSTOMER1)---(300ms)--->(CUSTOMER2)
 * <br/>(PIZZERIA)&lt;----------------400ms-----------------(         )
 * </pre></blockquote>
 *
 * <p>In this example, all the time required for the delivery boy to deliver all orders
 * and come back to pizzeria equals 800ms. This implementation follows this simplified logic.
 *
 * <p>Constructed from the JSON data, as specified in
 * {@link ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaImpl#fromJson(InputStream)}.
 *
 * <p>Uses the shared {@link java.util.concurrent.ScheduledExecutorService}
 * of {@link ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaImpl}.
 */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        working.set(true);
        waitForOrders();
    }

    private void waitForOrders() {
         currentTaskFuture = pizzeriaDeliveryBoyService.submit(() -> {
             if (!working.get() && pizzeriaDeliveryBoyService.getWarehouse().isEmpty()) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void forceStop() {
        currentTaskFuture.cancel(true);
        working.set(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopAfterCompletion() {
        working.set(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWorking() {
        return working.get();
    }
}

