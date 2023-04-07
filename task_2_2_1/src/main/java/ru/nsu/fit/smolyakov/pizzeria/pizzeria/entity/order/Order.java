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
        if (status.getPreviousStatus() == this.status) {
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
         * Created, waiting for acceptation. The first possible status.
         */
        CREATED("Created, waiting for acceptation", null),

        /**
         * Accepted, waiting for baker. May be set only and only after {@link #CREATED}.
         */
        ACCEPTED("Accepted, waiting for baker", CREATED),

        /**
         * Being baked. May be set only and only after {@link #ACCEPTED}.
         */
        BEING_BAKED("Being baked", ACCEPTED),

        /**
         * Baked, waiting for warehouse. May be set only and only after {@link #BEING_BAKED}
         */
        WAITING_FOR_WAREHOUSE("Baked, waiting for warehouse", BEING_BAKED),

        /**
         * Accepted on warehouse, waiting for delivery. May be set only and only after {@link #WAITING_FOR_WAREHOUSE}.
         */
        WAITING_FOR_DELIVERY("Accepted on warehouse, waiting for delivery", WAITING_FOR_WAREHOUSE),

        /**
         * In delivery. May be set only and only after {@link #WAITING_FOR_DELIVERY}.
         */
        IN_DELIVERY("In delivery", WAITING_FOR_DELIVERY),

        /**
         * Delivered. May be set only and only after {@link #IN_DELIVERY}.
         */
        DONE("Delivered", IN_DELIVERY);

        private final String caption;
        private final Status previousStatus;

        Status(String caption, Status previousStatus) {
            this.caption = caption;
            this.previousStatus = previousStatus;
        }

        /**
         * Returns human-readable meaning of current status.
         *
         * @return a caption
         */
        public String getCaption() {
            return caption;
        }

        /**
         * Returns a {@code Status} that is supposed to be set before this one.
         *
         * @return a previous status
         */
        public Status getPreviousStatus() {
            return previousStatus;
        }
    }
}
