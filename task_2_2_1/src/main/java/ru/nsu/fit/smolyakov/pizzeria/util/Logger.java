package ru.nsu.fit.smolyakov.pizzeria.util;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;

public enum Logger {
    INSTANCE;

    private final java.util.logging.Logger logger
        = java.util.logging.Logger.getLogger("pizzeria");

    private final String format = "~%s: [%d], [%s];%n";

    public void info(Order order) {
        logger.info(
            format.formatted(
                order.getPizzeriaOrderService(),
                order.getId(),
                order.getStatus().getCaption()
            )
        );
    }
}
