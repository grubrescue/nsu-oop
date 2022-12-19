package ru.nsu.fit.smolyakov.diary.executable;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class ArgumentsParser {
    void a() {
        Option opt = Option.builder("opt")
                .argName("n")
                .hasArg()
                .desc("n")
                .build();

        Options options = new Options();
        options.addOption(opt);
        // TODO everything
    }
}
