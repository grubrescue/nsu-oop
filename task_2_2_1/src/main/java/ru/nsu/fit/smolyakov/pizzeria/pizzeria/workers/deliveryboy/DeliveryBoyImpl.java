package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.deliveryboy;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import ru.nsu.fit.smolyakov.pizzeria.PizzeriaDeliveryBoyService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaBakerService;

public class DeliveryBoyImpl implements DeliveryBoy {
    @JsonManagedReference
    private PizzeriaDeliveryBoyService pizzeriaDeliveryBoyService;

    @Override
    public void deliver() {

    }
}
