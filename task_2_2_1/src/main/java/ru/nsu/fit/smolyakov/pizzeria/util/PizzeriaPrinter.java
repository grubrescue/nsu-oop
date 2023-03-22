package ru.nsu.fit.smolyakov.pizzeria.util;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.Order;

import java.io.PrintStream;

public class PizzeriaPrinter {
    private final PrintStream printStream;
    private static final String format = "~%s: [%d], [%s];%n";

    public PizzeriaPrinter(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void print(Order order) {
        printStream.printf(format,
            order.getPizzeriaOrderService().getPizzeriaName(),
            order.getId(),
            order.getStatus().getCaption());
    }
}
