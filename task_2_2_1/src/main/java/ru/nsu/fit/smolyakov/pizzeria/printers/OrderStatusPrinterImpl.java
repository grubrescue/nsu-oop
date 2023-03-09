package ru.nsu.fit.smolyakov.pizzeria.printers;

import ru.nsu.fit.smolyakov.pizzeria.entity.Order;

public class OrderStatusPrinterImpl implements OrderStatusPrinter {
    private static final String format
        = "[%s], [%s];%n";

    @Override
    public void print(Order order) {
        System.out.printf(format, order.getId(), order.getStatus().getCaption());
    }
}
