package ru.nsu.fit.smolyakov.pizzeria.customer;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOrderService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.OrderInformationService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;
import ru.nsu.fit.smolyakov.pizzeria.util.TasksExecutor;

import java.util.concurrent.ScheduledExecutorService;

/**
 * An implementation of {@link FrequentCustomer} interface.
 * May be instantiated by
 * {@link #FrequentCustomerImpl(OrderDescription, PizzeriaOrderService, int)}
 * constructor.
 */
public class FrequentCustomerImpl implements FrequentCustomer {
    private final OrderDescription orderDescription;
    private final PizzeriaOrderService pizzeriaOrderService;
    private final int frequencyMillis;
    private final ScheduledExecutorService executor;

    /**
     * Constructs an instance of {@code FrequentCustomerImpl}.
     *
     * @param executor an executor to put tasks into
     * @param orderDescription a description of performed repeated orders
     * @param pizzeriaOrderService a pizzeria to order pizza in
     * @param frequencyMillis a frequency with whom orders will be
     *                        performed by {@link #start(int)} method.
     */
    public FrequentCustomerImpl(ScheduledExecutorService executor,
                                OrderDescription orderDescription,
                                PizzeriaOrderService pizzeriaOrderService,
                                int frequencyMillis) {
        this.executor = executor;
        this.orderDescription = orderDescription;
        this.pizzeriaOrderService = pizzeriaOrderService;
        this.frequencyMillis = frequencyMillis;
    }

    /**
     * Makes an order in this {@code FrequentCustomer}'s favourite
     * pizzeria.
     *
     * Prints to {@link System#out} when a regular order is made,
     * either successfully or not.
     */
    @Override
    public void order() {
        System.out.printf("(Customer %dms) Wanna order pizza... %n%n", frequencyMillis);
        pizzeriaOrderService.makeOrder(orderDescription)
            .ifPresentOrElse(
                order ->
                    System.out.printf("(Customer %dms) Pizza %d ordered! %n%n",
                        frequencyMillis, order.getId()),
                () ->
                    System.out.printf("(Customer %dms) Pizza is not ordered, as pizzeria is not working! %n%n",
                        frequencyMillis)
            );
    }

    /**
     * Starts ordering pizza continuously.
     *
     * <p>This method reuses {@link #order()}
     * method.
     *
     * <p>All orders are processed in separated tasks
     * which are to be executed by {@link TasksExecutor}
     * singleton.
     *
     * @param times amount of times to order
     */
    @Override
    public void start(int times) {
        for (int i = 0; i < times; i++) {
            TasksExecutor.INSTANCE.schedule(
                this::order,
                i * frequencyMillis
            );
        }
    }
}
