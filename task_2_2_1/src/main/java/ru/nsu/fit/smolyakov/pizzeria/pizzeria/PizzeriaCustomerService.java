package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.OrderInformationService;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.description.OrderDescription;

import java.util.Optional;

@JsonDeserialize(as = PizzeriaImpl.class)
public interface PizzeriaCustomerService {
    String getPizzeriaName();

    boolean isWorking();

    Optional<OrderInformationService> makeOrder(OrderDescription orderDescription);
}
