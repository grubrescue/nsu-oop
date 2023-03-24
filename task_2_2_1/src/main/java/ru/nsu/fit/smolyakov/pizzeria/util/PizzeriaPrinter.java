package ru.nsu.fit.smolyakov.pizzeria.util;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.OrderInformationService;

import java.io.PrintStream;

public class PizzeriaPrinter {
    private static final String format = "~ %s: [%d], [%s];%n";
    private final PrintStream printStream;

    public PizzeriaPrinter(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void print(OrderInformationService order) {
        printStream.printf(format,
            order.getPizzeriaOrderService().getPizzeriaName(),
            order.getId(),
            order.getStatus().getCaption());
    }
}
