package ru.nsu.fit.smolyakov.pizzeria.entity;

import ru.nsu.fit.smolyakov.pizzeria.worker.OrderQueue;

public class Order {

    public enum Status {
        CREATED("Created"),
        BEING_BAKED("Being baked"),
        WAITING_FOR_DELIVERY("Waiting for delivery"),
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

    private Status status = Status.CREATED;
    private final int id;

    private final OrderDescription orderDescription;
    public Order(int id, Status status, OrderDescription orderDescription) {
        this.status = status;
        this.id = id;
        this.orderDescription = orderDescription;
    }

    public Order create(int id, OrderDescription orderDescription) {
        return new Order(id, Status.CREATED, orderDescription);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }
    public OrderDescription getOrderDescription() {
        return orderDescription;
    }
}
