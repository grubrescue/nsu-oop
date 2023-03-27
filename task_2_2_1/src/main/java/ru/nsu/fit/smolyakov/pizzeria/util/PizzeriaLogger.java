package ru.nsu.fit.smolyakov.pizzeria.util;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.OrderInformationService;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class PizzeriaLogger {
    private static final String orderFormat = "(%s) ~ %s: [%d], [%s];%n";
    private static final String messageFormat = "(%s) ~ %s: %s;%n";

    private final String pizzeriaName;

    public PizzeriaLogger(String pizzeriaName) {
        this.pizzeriaName = pizzeriaName;
    }

    public void orderInfo(OrderInformationService order) {
        System.out.printf(
            orderFormat,
            LocalTime.now(),
            pizzeriaName,
            order.getId(),
            order.getStatus().getCaption()
        );
    }

    public void message(String message) {
        System.out.printf(
            messageFormat,
            LocalTime.now(),
            pizzeriaName,
            message
        );
    }
}
