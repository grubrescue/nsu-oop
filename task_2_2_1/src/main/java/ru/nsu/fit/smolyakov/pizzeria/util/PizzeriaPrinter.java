package ru.nsu.fit.smolyakov.pizzeria.util;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.OrderInformationService;

import java.io.PrintStream;

public class PizzeriaPrinter {
    private static final String format = "~ %s: [%d], [%s];%n";

    public static String orderFormatted (OrderInformationService order) {
        return format.formatted(
            order.getPizzeriaOrderService().getPizzeriaName(),
            order.getId(),
            order.getStatus().getCaption()
        );
    }
}
