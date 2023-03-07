package ru.nsu.fit.smolyakov.pizzeria.printers;

import ru.nsu.fit.smolyakov.pizzeria.entity.Order;

public class OrderStatusPrinterImpl implements OrderStatusPrinter {
    private static final String format
        = "[%s], [%s];";
    @Override
    public void print(Order order) {
        System.out.printf((format) + "%n", order.getId(), order.getStatus().getCaption());
    }
}
