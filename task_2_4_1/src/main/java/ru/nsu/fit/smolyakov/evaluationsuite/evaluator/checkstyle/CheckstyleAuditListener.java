package ru.nsu.fit.smolyakov.evaluationsuite.evaluator.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * A listener for checkstyle.
 */
@Log4j2
public class CheckstyleAuditListener implements AuditListener {
    private final CheckstyleResult result;

    /**
     * Creates a listener.
     * @param result checkstyle result, to which errors and warnings will be added
     */
    public CheckstyleAuditListener(@NonNull CheckstyleResult result) {
        this.result = result;
    }

    /**
     * Called when the audit is started. Does nothing.
     *
     * @param event the event details
     */
    @Override
    public void auditStarted(AuditEvent event) {
    }

    /**
     * Called when the audit is finished. Does nothing.
     *
     * @param event the event details
     */
    @Override
    public void auditFinished(AuditEvent event) {
    }

    /**
     * Called when a file is started. Does nothing.
     *
     * @param event the event details
     */
    @Override
    public void fileStarted(AuditEvent event) {
    }

    /**
     * Called when a file is finished. Does nothing.
     *
     * @param event the event details
     */
    @Override
    public void fileFinished(AuditEvent event) {
    }

    /**
     * Called when a violation occurs. Adds an error.
     *
     * @param event the event details
     */
    @Override
    public void addError(AuditEvent event) {
        log.debug("Checkstyle error: {}", event.getMessage());
        result.addError();
    }

    /**
     * Called when an exception occurs. Adds a warning.
     *
     * @param event the event details
     * @param throwable the exception
     */
    @Override
    public void addException(AuditEvent event, Throwable throwable) {
        log.debug("Checkstyle exception: {}", event.getMessage());
        result.addWarning();
    }
}
