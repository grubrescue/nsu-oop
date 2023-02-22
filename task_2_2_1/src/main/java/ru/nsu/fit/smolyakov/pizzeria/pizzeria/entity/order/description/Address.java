package ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description;

/**
 * Represents an address of a customer.
 * Supposed to be a part of {@link OrderDescription}.
 *
 * @param address            a string representation of a customer's address.
 * @param deliveryTimeMillis a time to be spent by a
 *                           {@link ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.deliveryboy.DeliveryBoy}
 *                           to deliver a pizza from pizzeria to a customer.
 */
public record Address(
    String address,
    int deliveryTimeMillis
) {
}
