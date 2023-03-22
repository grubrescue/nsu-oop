package ru.nsu.fit.smolyakov.pizzeria;

import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaImpl;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaOrderService;

import java.io.File;

public class Application {
    public static void main(String[] args) {
        PizzeriaOrderService pizzeriaOrderService =
            PizzeriaImpl.fromJson(Application.class.getResourceAsStream("/PizzeriaConfiguration.json"));

//        System.out.println(pizzeriaOrderService.getPizzeriaName());
    }
}