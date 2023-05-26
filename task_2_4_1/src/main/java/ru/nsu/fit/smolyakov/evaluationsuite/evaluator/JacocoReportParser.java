package ru.nsu.fit.smolyakov.evaluationsuite.evaluator;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JacocoReportParser {
    private static final XmlMapper xmlMapper = new XmlMapper();

    public record Counter(String type, int missed, int covered) {
    }

    List<Counter> counter
        = new ArrayList<>();

    public static void sdelat() throws IOException {
        var parser = xmlMapper.readValue(new File("../task_1_1_1/build/reports/jacoco/test/jacocoTestReport.xml"),
            JacocoReportParser.class);

        System.out.println(parser);
    }
}
