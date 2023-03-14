package ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity;

public record OrderDescription(
    Address address,
    String additionalInformation
) {
}
