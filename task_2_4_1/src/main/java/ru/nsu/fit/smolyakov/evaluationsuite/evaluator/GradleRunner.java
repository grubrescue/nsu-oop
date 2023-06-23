package ru.nsu.fit.smolyakov.evaluationsuite.evaluator;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.log4j.Log4j2;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

import java.io.File;
import java.util.List;

/**
 * A class to run Gradle tasks.
 */
@Getter
@Builder
@Log4j2
@NonNull
public class GradleRunner { // TODO rename
    private final String projectPath;

    @Singular
    private final List<GradleTask> tasks;

    /**
     * Runs Gradle task with given name.
     *
     * @param connection Gradle connection
     * @param task       task name
     * @return true on success
     */
    private boolean runTask(ProjectConnection connection, String task) {
        log.info("Running task {}", task);

        try {
            connection.newBuild()
                .forTasks(task)
                .run();
        } catch (Exception e) {
            log.error("Gradle task {} failed; cause: {}", task, e.getMessage());
            return false;
        }

        log.info("Task {} finished successfully", task);
        return true;
    }

    /**
     * Runs all tasks in {@link GradleRunner#tasks} list.
     * For each task, if one finished successfully, runs {@link GradleTask#runIfSuccess()}.
     */
    public void run() {
        try (var connection = GradleConnector.newConnector()
            .forProjectDirectory(new File(projectPath))
            .connect()
        ) {
            log.info("Starting Gradle evaluator");

            tasks.forEach(task -> {
                if (runTask(connection, task.name())) {
                    task.runIfSuccess().run();
                }
            });
        } catch (Exception e) {
            log.error("Gradle evaluation failed; cause: {}", e.getMessage());
        }
    }

    /**
     * A record to store Gradle task name and {@link Runnable} to run if task finished successfully.
     *
     * @param name         task name
     * @param runIfSuccess {@link Runnable} to run if task finished successfully
     */
    public record GradleTask(String name, Runnable runIfSuccess) {
    }
}