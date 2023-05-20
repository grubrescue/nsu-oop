package ru.nsu.fit.smolyakov.labchecker.checker;

import lombok.AllArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import ru.nsu.fit.smolyakov.labchecker.entity.Student;

import java.io.File;
import java.io.IOException;


@AllArgsConstructor
public class EvaluationRunner {
    public final String TMP_DIR = ".checks_tmp/" + System.currentTimeMillis() + "/";

//    private final MainEntity mainEntity;

    public void tmp(Student student) throws GitAPIException, IOException { // TODO todo
        var dir = new File(TMP_DIR + student.getNickName());

        var repoCloneCommand = Git.cloneRepository()
            .setURI(student.getRepoUrl())
            .setDirectory(dir)
            .setCloneAllBranches(true);

//        Git git = new Git(new FileRepository(".checks_tmp/1684585039294/evangelionexpert/.git"));
//        git.fetch()
//            .f
//
//        var taskStatus = student.getAssignmentStatusList().get(0);
//
//        git.branchList()
//            .call()
//            .forEach(ref -> System.out.println(ref.getName()));
//
//        git.checkout().setName(taskStatus.getBranch()).call();

        //        var iter = git.log().addPath(taskStatus.getTaskNameAlias()).call();
//        iter.forEach(commit -> System.out.println(commit));



//        var iter2 = git.log().addPath(taskStatus.getTaskNameAlias()).call();
//        iter2.forEach(commit -> System.out.println(commit));



        try (Git repo = repoCloneCommand.call()) {
            var ref = repo.checkout()
                .setName("task-1-1-1-fork")
                .setCreateBranch(true)
                .call();

            System.out.println(ref);
            System.out.println(repo.branchList().call());


//            var refList = repo.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
//
//            refList
//                .forEach(ref -> {
//                    System.out.println(ref.getName());
//                    try {
//                        repo.fetch().setRemote(ref.getName()).call();
//                    } catch (GitAPIException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//
//            System.out.println("-----");
//            repo.branchList()
//            .call()
//            .forEach(ref -> System.out.println(ref.getName()));
//            var assignmentStatus = student.getAssignmentStatusList().get(0);
//
//            var branchName = assignmentStatus.getBranch();
//            var checkoutCommand = repo.checkout().setName(branchName);
//
//            try {
//                checkoutCommand.call();
//            } catch (GitAPIException e) {
//                throw new RuntimeException(e);
//            }

        }
    }

    private void processAssignment() {

    }

    private void processStudent(Student student) throws GitAPIException, IOException {
        var dir = new File(TMP_DIR + student.getNickName());

        var repoCloneCommand = Git.cloneRepository()
            .setURI(student.getRepoUrl())
            .setDirectory(dir)
            .setCloneAllBranches(true);

        try (Git repo = repoCloneCommand.call()) {
            student.getAssignmentStatusList()
                .forEach(assignmentStatus -> {
                    var branchName = assignmentStatus.getBranch();
                    var checkoutCommand = repo.checkout().setName(branchName);

                    try {
                        checkoutCommand.call();
                    } catch (GitAPIException e) {
                        throw new RuntimeException(e);
                    }
                }
                );
        }
    }

//    public void runAll() {
//        try {
////            processStudent();
//        } catch (GitAPIException | IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
