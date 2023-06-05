package ru.nsu.fit.smolyakov.evaluationsuite.evaluator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * A class to parse Jacoco report XML.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@JacksonXmlRootElement(localName = "name")
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class JacocoReportParser {
    private static final XmlMapper xmlMapper
        = new XmlMapper();
    @JacksonXmlProperty(localName = "name")
    @Getter
    private String name;
    @JacksonXmlProperty(localName = "counter")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Counter> counterList;

    /**
     * Parses .xml Jacoco report and creates
     * a new instance of {@link JacocoReportParser}.
     */
    public static JacocoReportParser parse(File reportXml) throws IOException {
        return xmlMapper.readValue(
            reportXml,
            JacocoReportParser.class);
    }

    /**
     * Returns counter by type.
     *
     * @param counterType a counter type
     * @return {@link Optional} with counter if it exists,
     * {@link Optional#empty()} otherwise
     */
    public Optional<Counter> getCounterByType(CounterType counterType) {
        return counterList.stream()
            .filter(counter -> counter.type == counterType)
            .findFirst();
    }

    /**
     * Returns coverage by counter type.
     *
     * @param counterType counter type
     * @return {@link Optional} with coverage value if counter exists,
     * {@link Optional#empty()} otherwise
     */
    public Optional<Double> getCoverageByType(CounterType counterType) {
        return getCounterByType(counterType)
            .map(counter ->
                (double) counter.covered / (counter.covered + counter.missed)
            );
    }

    /**
     * Jacoco counter type.
     * See <a href="https://www.jacoco.org/jacoco/trunk/doc/counters.html">official documentation</a>
     * for details.
     */
    public enum CounterType {
        /**
         * Instruction counter.
         */
        INSTRUCTION,

        /**
         * Branch counter.
         */
        BRANCH,

        /**
         * Line counter.
         */
        LINE,

        /**
         * Complexity counter.
         */
        COMPLEXITY,

        /**
         * Method counter.
         */
        METHOD,

        /**
         * Class counter.
         */
        CLASS
    }

    /**
     * A record to store Jacoco counter.
     *
     * @param type    counter type
     * @param missed  missed
     * @param covered covered
     */
    public record Counter(
        @JsonProperty("type") CounterType type,
        @JsonProperty("missed") int missed,
        @JsonProperty("covered") int covered) {
    }
}
