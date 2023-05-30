package ru.nsu.fit.smolyakov.consoleinterpreter.processor;

import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.AbstractCommandProvider;

public class Processor {
    private AbstractCommandProvider provider;

    public Processor(AbstractCommandProvider rootProvider) {
        this.provider = rootProvider;
    }




}
