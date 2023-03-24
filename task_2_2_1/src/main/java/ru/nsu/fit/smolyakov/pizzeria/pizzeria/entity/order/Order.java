package ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOrderService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;

public class Order implements OrderInformationService {
    public enum Status {
        CREATED("Created, waiting for acception"),
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

        public String getCaption() {
            return caption;
        }
    }

    private final PizzeriaOrderService pizzeriaOrderService;
    private final int id;
    private final OrderDescription orderDescription;

    private Status status;

    private Order(PizzeriaOrderService pizzeriaOrderService,
                  int id,
                  Status status,
                  OrderDescription orderDescription) {
        this.pizzeriaOrderService = pizzeriaOrderService;
        this.status = status;
        this.id = id;
        this.orderDescription = orderDescription;
    }

    public static Order create(PizzeriaOrderService pizzeriaOrderService,
                               int id,
                               OrderDescription orderDescription) {
        return new Order(pizzeriaOrderService, id, Status.CREATED, orderDescription);
    }

    public PizzeriaOrderService getPizzeriaOrderService() {
        return pizzeriaOrderService;
    }

    public synchronized Status getStatus() {
        return status;
    }

    public synchronized void setStatus(Status status) {
        if (status.ordinal() - this.status.ordinal() == 1) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("inappropriate status change (some steps are missed)");
        }
    }

    public int getId() {
        return id;
    }

    public OrderDescription getOrderDescription() {
        return orderDescription;
    }
}
