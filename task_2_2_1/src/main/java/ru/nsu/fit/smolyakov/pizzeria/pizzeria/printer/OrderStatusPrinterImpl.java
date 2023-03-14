package ru.nsu.fit.smolyakov.pizzeria.pizzeria.printer;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;

public class OrderStatusPrinterImpl implements OrderStatusPrinter {
    private static final String format
        = "[%s], [%s];%n";

    @Override
    public void print(Order order) {
        System.out.printf(format, order.getId(), order.getStatus().getCaption());
    }
}
