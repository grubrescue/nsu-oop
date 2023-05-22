package ru.nsu.fit.smolyakov.labchecker.checker;


import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.gradle.tooling.*;

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
//        AtomicBoolean result = new AtomicBoolean(false);
//        ProgressListener listener = progressEvent -> {
//            if (progressEvent instanceof TaskFinishEvent taskFinishEvent) {
//                var actualTaskName = taskFinishEvent.getDescriptor().getName().substring(1);
//
//                if (!actualTaskName.equals(task)) {
//                    return;
//                }
//
//                if (taskFinishEvent.getResult() instanceof TaskFailureResult) {
//                    result.set(false);
//                    log.info("task {} failed", task);
//                } else if (taskFinishEvent.getResult() instanceof TaskSuccessResult) {
//                    result.set(true);
//                    log.info("task {} successful", task);
//                } else {
//                    throw new RuntimeException("это што спрашивается такое" + taskFinishEvent.getResult().getClass());
//                    //TODO сгыещь custom exception
//                }
//            }
//        };
        AtomicBoolean result = new AtomicBoolean(false);
        var a = new ResultHandler<Void>() {
            @Override
            public void onComplete(Void unused) {
                result.set(true);
            }

            @Override
            public void onFailure(GradleConnectionException failure) {
                log.error(failure);
            }
        };

        try {
            connection.newBuild()
                .forTasks(task)
                .run(a);
        } catch (Exception e) {
            log.error("Gradle task {} failed because of {}", task, e.getMessage());
        }

        return result.get();
    }

    public void run() {
        try (var connection = GradleConnector.newConnector()
            .forProjectDirectory(new File(projectPath))
            .connect()
        ) {
            log.info("Starting Gradle evaluator");

            runTask(connection, "clean");

            tasks.forEach(task -> {
                if (runTask(connection, task.name())) {
                    task.runIfSuccess().run();
                }
            });
        } catch (Exception e) {
            log.error("Gradle evaluation failed because of {}", e.getMessage());
        }
    }
}