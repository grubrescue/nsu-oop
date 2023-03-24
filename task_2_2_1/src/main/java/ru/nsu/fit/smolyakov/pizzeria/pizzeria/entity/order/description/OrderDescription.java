package ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description;

public record OrderDescription(
    Address address,
    String additionalInformation
) {
}
