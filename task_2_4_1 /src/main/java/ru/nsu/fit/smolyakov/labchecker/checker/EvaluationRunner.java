package ru.nsu.fit.smolyakov.labchecker.checker;

import lombok.AllArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import ru.nsu.fit.smolyakov.labchecker.entity.CheckerScript;
import ru.nsu.fit.smolyakov.labchecker.entity.group.Student;

import java.io.File;
import java.io.IOException;


@AllArgsConstructor
public class EvaluationRunner { //  TODO todo
    public final String TMP_DIR = ".dsl_tmp/" + System.currentTimeMillis() + "/";

    private CheckerScript checkerScript;

    private void processStudent(Student student) throws GitAPIException, IOException {
        var repoName = student.getRepo();
        var uri = checkerScript.getConfiguration().getGit().getRepoLink(repoName);

        var dir = new File(TMP_DIR + repoName);

        var repoCloneCommand = Git.cloneRepository()
            .setURI(uri)
            .setDirectory(dir)
            .setCloneAllBranches(true);

        try (Git masterRepo = repoCloneCommand.call()) {
            masterRepo.checkout().setName("task-1-1-1").call();

            checkerScript.getCourse()
                .getTasks()
                .getList()
                .forEach((task) -> {
//                    if (student)
                });


        }
    }

    public void runAll() {
//        checkerScript.getGroup().getStudents().getList().forEach(student -> {
//            try {
//            } catch (GitAPIException | IOException e) {
//            }
//        });
        try {
            processStudent(checkerScript.getGroup().getStudents().getList().get(6));
        } catch (GitAPIException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
