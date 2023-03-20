package ru.nsu.fit.smolyakov.pizzeria.pizzeria.workers.baker;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import ru.nsu.fit.smolyakov.pizzeria.pizzeria.PizzeriaBakerService;

public class BakerImpl implements Baker {
    @JsonManagedReference
    private PizzeriaBakerService pizzeriaBakerService;

    @Override
    public void cook() {

    }
}
