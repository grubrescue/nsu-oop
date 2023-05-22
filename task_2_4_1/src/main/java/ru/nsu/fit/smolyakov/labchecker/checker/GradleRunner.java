package ru.nsu.fit.smolyakov.labchecker.checker;


import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

import java.io.File;
import java.util.List;

@Value
@Builder
@Log4j2
public class GradleRunner { // TODO rename
    @Getter
    String projectPath;

    @Singular
    @Getter
    List<GradleTask> tasks;

    public record GradleTask(String name, Runnable runIfSuccess) {
    }

    /**
     *
     * @param connection
     * @param task
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

    public void run() {
        try (var connection = GradleConnector.newConnector()
            .forProjectDirectory(new File(projectPath))
            .connect()
        ) {
            log.info("Starting Gradle evaluator");
//
//            log.info("Running cleaning");
//            runTask(connection, "build"); // TODO таймаут???

            tasks.forEach(task -> {
                if (runTask(connection, task.name())) {
                    task.runIfSuccess().run();
                }
            });
        } catch (Exception e) {
            log.error("Gradle evaluation failed; cause: {}", e.getMessage());
        }
    }
}