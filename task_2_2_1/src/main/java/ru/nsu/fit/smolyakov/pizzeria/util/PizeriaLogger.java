package ru.nsu.fit.smolyakov.pizzeria.util;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;

import java.util.logging.Logger;

// TODO может его засунуть куда нибудь внутрь пиццерии? будет лучше ж
public enum PizeriaLogger {
    INSTANCE;

    private final Logger logger
        = Logger.getLogger("pizzeria");

    private final String format = "~%s: [%d], [%s];%n";

    public void info(Order order) {
        logger.info(
            format.formatted(
                order.getPizzeriaOrderService().getPizzeriaName(),
                order.getId(),
                order.getStatus().getCaption()
            )
        );
    }
}
