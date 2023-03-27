package ru.nsu.fit.smolyakov.pizzeria.util;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.OrderInformationService;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class PizzeriaLogger {
    private static final String orderFormat = "(%s) ~ %s: [%d], [%s];%n";
    private static final String messageFormat = "(%s) ~ %s: %s;%n";

    public static void orderInfo(LocalTime time, OrderInformationService order) {
        System.out.printf(
            orderFormat,
            time,
            order.getPizzeriaOrderService().getPizzeriaName(),
            order.getId(),
            order.getStatus().getCaption()
        );
    }

//    public static void message(ZonedDateTime time, String pizzeriaName, String message) {
//        System.out.printf(
//            messageFormat,
//            pizzeriaName,
//            time,
//            message
//        );
//    }
}
