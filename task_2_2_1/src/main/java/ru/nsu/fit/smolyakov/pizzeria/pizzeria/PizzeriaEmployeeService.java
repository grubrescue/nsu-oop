package ru.nsu.fit.smolyakov.pizzeria.pizzeria;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.entity.order.OrderInformationService;

@JsonDeserialize(as = PizzeriaImpl.class)
public interface PizzeriaEmployeeService {
    void execute(Runnable task);
    void schedule(int delayMillis, Runnable task);
}
