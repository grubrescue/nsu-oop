package ru.nsu.fit.smolyakov.labchecker.checker;


import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.gradle.tooling.BuildException;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.events.OperationType;
import org.gradle.tooling.events.ProgressListener;
import org.gradle.tooling.events.task.TaskFailureResult;
import org.gradle.tooling.events.task.TaskFinishEvent;
import org.gradle.tooling.events.task.TaskSuccessResult;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
        AtomicBoolean result = new AtomicBoolean(false);
        ProgressListener listener = progressEvent -> {
            if (progressEvent instanceof TaskFinishEvent taskFinishEvent) {
                var actualTaskName = taskFinishEvent.getDescriptor().getName().substring(1);

                if (!actualTaskName.equals(task)) {
                    return;
                }

                if (taskFinishEvent.getResult() instanceof TaskFailureResult) {
                    result.set(false);
                } else if (taskFinishEvent.getResult() instanceof TaskSuccessResult) {
                    result.set(true);
                } else {
                    throw new RuntimeException("это што спрашивается такое" + taskFinishEvent.getResult().getClass());
                    //TODO сгыещь custom exception
                }
            }
        };

        try {
            connection.newBuild()
                .forTasks(task)
                .addProgressListener(listener, OperationType.TASK)
                .run();
        } catch (BuildException ignored) {
        }

        return result.get();
    }

    public void run() {
        try (var connection = GradleConnector.newConnector()
            .forProjectDirectory(new File(projectPath))
            .connect()
        ) {
            log.info("Starting Gradle evaluator");
            connection.newBuild().forTasks("clean").run();
            log.info("clean successful");

            tasks.forEach(task -> {
                if (runTask(connection, task.name())) {
                    task.runIfSuccess().run();
                    log.info("task {} successful", task.name());
                } else {
                    log.info("task {} failed", task.name());
                }
            });
        }
    }
}
