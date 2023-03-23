package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.OrderDescription;

@JsonDeserialize(as = PizzeriaImpl.class)
public interface PizzeriaOrderService {
    String getPizzeriaName();

    boolean isWorking();

    /**
     * @param orderDescription
     * @return
     */
    int makeOrder(OrderDescription orderDescription);
}
