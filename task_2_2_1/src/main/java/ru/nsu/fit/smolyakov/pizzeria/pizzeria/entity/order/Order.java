package ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaCustomerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;
import ru.nsu.fit.smolyakov.pizzeria.util.PizzeriaLogger;

/**
 * Represents an order, created by {@link ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaImpl}
 * and used during the whole lifetime of the former.
 */
public class Order implements OrderInformationService {
    /**
     * Represents current status of this order.
     */
    public enum Status {
        CREATED("Created, waiting for acceptation"),
        ACCEPTED("Accepted, waiting for baker"),
        BEING_BAKED("Being baked"),
        WAITING_FOR_WAREHOUSE("Baked, waiting for warehouse"),
        WAITING_FOR_DELIVERY("Accepted on warehouse, waiting for delivery"),
        IN_DELIVERY("In delivery"),
        DONE("Delivered");

        private final String caption;

        private Status(String caption) {
            this.caption = caption;
        }

        /**
         * Returns human-readable meaning of current status.
         *
         * @return a caption
         */
        public String getCaption() {
            return caption;
        }
    }

    private final PizzeriaCustomerService pizzeriaCustomerService;
    private final int id;
    private final OrderDescription orderDescription;

    private final PizzeriaLogger logger;

    private Status status;

    public Order(PizzeriaCustomerService pizzeriaCustomerService,
                 PizzeriaLogger logger,
                 int id,
                 Status status,
                 OrderDescription orderDescription) {
        this.pizzeriaCustomerService = pizzeriaCustomerService;
        this.logger = logger;
        this.status = status;
        this.id = id;
        this.orderDescription = orderDescription;

        logger.orderInfo(this);
    }

    @Override
    public PizzeriaCustomerService getPizzeriaCustomerService() {
        return pizzeriaCustomerService;
    }

    /**
     *
     * @return
     */
    public synchronized Status getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public synchronized void setStatus(Status status) {
        if (status.ordinal() - this.status.ordinal() == 1) {
            this.status = status;
            logger.orderInfo(this);
        } else {
            throw new IllegalArgumentException("inappropriate status change (some steps are missed)");
        }
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public OrderDescription getOrderDescription() {
        return orderDescription;
    }
}
