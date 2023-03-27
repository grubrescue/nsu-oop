package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.Order;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.OrderInformationService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.baker.Baker;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.deliveryboy.DeliveryBoy;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.orderqueue.OrderQueue;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.warehouse.Warehouse;
import ru.nsu.fit.smolyakov.pizzeria.util.PizzeriaLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PizzeriaImpl implements PizzeriaCustomerService,
    PizzeriaEmployeeService,
    PizzeriaOwnerService,
    PizzeriaBakerService,
    PizzeriaDeliveryBoyService {

    private final static ObjectMapper mapper = new ObjectMapper();

    @JsonIgnore
    private ScheduledExecutorService executorService;

    @JsonIgnore
    private final PizzeriaLogger logger;

    @JsonProperty("name")
    private String pizzeriaName;

    @JsonProperty("orderQueue")
    @JsonManagedReference(value = "orderQueue")
    private OrderQueue orderQueue;

    @JsonProperty("warehouse")
    @JsonManagedReference(value = "warehouse")
    private Warehouse warehouse;

    @JsonProperty("bakers")
    @JsonManagedReference(value = "bakers")
    private List<Baker> bakerList;

    @JsonProperty("deliveryBoys")
    @JsonManagedReference(value = "deliveryBoys")
    private List<DeliveryBoy> deliveryBoyList;

    @JsonIgnore
    private final AtomicBoolean working = new AtomicBoolean(false);
    @JsonIgnore
    private int orderId = 0;

    @JsonCreator
    private PizzeriaImpl(@JsonProperty("name") String pizzeriaName) {
        this.pizzeriaName = pizzeriaName;
        this.logger = new PizzeriaLogger(pizzeriaName);
    }

    /**
     * {@inheritDoc}
     */
    public static PizzeriaOwnerService fromJson(InputStream stream) {
        try {
            return mapper.readValue(stream, PizzeriaImpl.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<OrderInformationService> makeOrder(OrderDescription orderDescription) {
        if (!working.get()) {
            return Optional.empty();
        }

        var order = new Order(this, logger, orderId++, Order.Status.CREATED, orderDescription);

        orderQueue.put(order);

        return Optional.of(order);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void start() {
        executorService =
            new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());

        working.set(true);

        orderQueue.start();
        bakerList.forEach(Baker::start);
        warehouse.start();
        deliveryBoyList.forEach(DeliveryBoy::start);

        logger.message("Started");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWorking() {
        return working.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void stop() {
        logger.message("Soft stop signal");

        working.set(false);
        bakerList.forEach(Baker::stopAfterCompletion);
        orderQueue.stopAfterCompletion();
        warehouse.stopAfterCompletion();
        deliveryBoyList.forEach(DeliveryBoy::stopAfterCompletion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void forceStop() {
        logger.message("Force stop signal");

        working.set(false);
        bakerList.forEach(Baker::forceStop);
        orderQueue.forceStop();
        deliveryBoyList.forEach(DeliveryBoy::forceStop);
        warehouse.forceStop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PizzeriaCustomerService getOrderService() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Warehouse getWarehouse() {
        return warehouse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderQueue getOrderQueue() {
        return orderQueue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPizzeriaName() {
        return pizzeriaName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<?> submit(Runnable task) {
        return executorService.submit(task, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<?> schedule(int delayMillis, Runnable task) {
        return executorService.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
    }
}

