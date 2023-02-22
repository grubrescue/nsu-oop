package ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description;

/**
 * Contains an information, used to
 * {@link ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaImpl#makeOrder(OrderDescription)}
 * in a pizzeria.
 *
 * @param address               an address of a customer
 * @param additionalInformation some additional information about dishes, ingredients, preferences etc.
 */
public record OrderDescription(
    Address address,
    String additionalInformation
) {
}
