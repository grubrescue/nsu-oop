package ru.nsu.fit.smolyakov.labchecker.checker;


import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.gradle.tooling.*;
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

//                if (!actualTaskName.equals(task)) {
//                    return;
//                }

                if (taskFinishEvent.getResult() instanceof TaskFailureResult res) {
                    if (actualTaskName.equals(task)) {
                        result.set(false);
                        log.info("Task {} successful", actualTaskName);
                    }
                } else if (taskFinishEvent.getResult() instanceof TaskSuccessResult) {
                    if (actualTaskName.equals(task)) {
                        result.set(true); // TODO
                        log.info("Task {} successful", actualTaskName);
                    }

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
        } catch (Exception e) {
            log.error("Task {} failed because of {}", task, e.getMessage());
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
                }
            });
        } catch (UnsupportedVersionException e) {
            log.error("Target Gradle version is not supported: {}", e.getMessage());
        } catch (GradleConnectionException e) {
            log.error("Gradle connection failed because of {}", e.getMessage());
        } catch (Exception e) {
            log.error("Gradle evaluation failed because of {}", e.getMessage());
        }
    }
}
