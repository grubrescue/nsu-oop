package ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaCustomerService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;
import ru.nsu.fit.smolyakov.pizzeria.util.PizzeriaLogger;

/**
 * Represents an order, created by {@link ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaImpl}
 * and used during the whole lifetime of the former.
 */
public class Order implements OrderInformationService {
    private final PizzeriaCustomerService pizzeriaCustomerService;
    private final int id;
    private final OrderDescription orderDescription;
    private final PizzeriaLogger logger;
    private Status status;

    /**
     * Constructs an instance of {@code order}.
     *
     * @param pizzeriaCustomerService a pizzeria customer service
     * @param logger                  a logger that belong to pizzeria
     * @param id                      an id given by pizzeria
     * @param status                  a status
     * @param orderDescription        an order description
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public PizzeriaCustomerService getPizzeriaCustomerService() {
        return pizzeriaCustomerService;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Status getStatus() {
        return status;
    }

    /**
     * Sets {@link Status} to a specified one.
     *
     * @param status specified status
     * @throws IllegalArgumentException if an inappropriate status change occurred
     *                                  (specifically, some steps are missed)
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
     * {@inheritDoc}
     */
    public int getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public OrderDescription getOrderDescription() {
        return orderDescription;
    }

    /**
     * Represents current status of this order.
     */
    public enum Status {
        /**
         * Created, waiting for acceptation.
         */
        CREATED("Created, waiting for acceptation"),

        /**
         * Accepted, waiting for baker.
         */
        ACCEPTED("Accepted, waiting for baker"),

        /**
         * Being baked.
         */
        BEING_BAKED("Being baked"),

        /**
         * Baked, waiting for warehouse.
         */
        WAITING_FOR_WAREHOUSE("Baked, waiting for warehouse"),

        /**
         * Accepted on warehouse, waiting for delivery.
         */
        WAITING_FOR_DELIVERY("Accepted on warehouse, waiting for delivery"),

        /**
         * In delivery.
         */
        IN_DELIVERY("In delivery"),

        /**
         * Delivered.
         */
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
}
