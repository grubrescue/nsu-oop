package ru.nsu.fit.smolyakov.labchecker.util;

import groovy.lang.Closure;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DSLUtil {
    public static void groovyDelegate(Object delegate, Closure<?> closure) {
        closure.setDelegate(delegate);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }

    /**
     * {@link DateTimeFormatter#ISO_LOCAL_DATE} format.
     * @param date
     * @return
     */
    public static LocalDate parseDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
