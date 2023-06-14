package ru.nsu.fit.smolyakov.evaluationsuite.evaluator.checkstyle;

import lombok.Getter;
import lombok.Setter;

/**
 * Checkstyle result.
 */
@Getter
@Setter
public class CheckstyleResult {
    private int warningsAmount = 0;
    private int errorsAmount = 0;

    /**
     * Adds a warning.
     */
    public void addWarning() {
        warningsAmount++;
    }

    /**
     * Adds an error.
     */
    public void addError() {
        errorsAmount++;
    }
}
