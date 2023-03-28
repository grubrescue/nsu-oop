package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.baker;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaBakerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.Order;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue.OrderQueue;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An implementation of {@link Baker} interface.
 *
 * <p>Behaves as if a baker was a finite automata - firstly, he waits for
 * an {@link Order} in a {@link OrderQueue}, then he does cooking,
 * and, finally, he tries to put a pizza to a warehouse. Latter is
 * incredibly small for a such a large pizzeria, so it locks every
 * single second.
 *
 * <p>In fact, all cooking process is just sleeping for a {@code cookingTimeMillis}
 * (specified in JSON).
 *
 * <p>Constructed from the JSON data, as specified in
 * {@link ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaImpl#fromJson(InputStream)}.
 *
 * <p>Uses the shared {@link java.util.concurrent.ScheduledExecutorService}
 * of {@link ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaImpl}.
 */
public class BakerImpl implements Baker {
    @JsonIgnore
    private final AtomicBoolean working = new AtomicBoolean(false);
    @JsonBackReference(value = "bakers")
    private PizzeriaBakerService pizzeriaBakerService;
    @JsonProperty("cookingTimeMillis")
    private int cookingTimeMillis;
    @JsonProperty("id")
    private int id;
    @JsonIgnore
    private Future<?> currentTaskFuture;

    private BakerImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        working.set(true);
        waitForOrder();
    }

    private void waitForOrder() {
        currentTaskFuture = pizzeriaBakerService.submit(() -> {
            if (!working.get() && pizzeriaBakerService.getOrderQueue().isEmpty()) {
                return;
            }

            var orderQueue = pizzeriaBakerService.getOrderQueue();
            var order = orderQueue.take();

            order.setStatus(Order.Status.BEING_BAKED);
            cookAndStore(order);
        });
    }

    private void cookAndStore(Order order) {
        currentTaskFuture = pizzeriaBakerService.schedule(
            cookingTimeMillis,
            () -> {
                order.setStatus(Order.Status.WAITING_FOR_WAREHOUSE);
                var warehouse = pizzeriaBakerService.getWarehouse();
                warehouse.put(order);
                this.waitForOrder();
            });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forceStop() {
        working.set(false);
        currentTaskFuture.cancel(true);
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
