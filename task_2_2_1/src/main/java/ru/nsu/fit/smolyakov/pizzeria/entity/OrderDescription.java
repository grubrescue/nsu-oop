package ru.nsu.fit.smolyakov.pizzeria.entity;

public record OrderDescription(
    Address address,
    String additionalInformation
) {
}
