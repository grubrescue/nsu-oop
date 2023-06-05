package ru.nsu.fit.smolyakov.evaluationsuite.evaluator;

import lombok.extern.log4j.Log4j2;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Log4j2
public class StudentRepository implements AutoCloseable {
    private final Git git;

    public StudentRepository(String uri, File directory) throws GitAPIException {
        git = Git.cloneRepository()
            .setURI(uri)
            .setDirectory(directory)
            .setCloneAllBranches(true)
            .call();
    }

    public String getAbsolutePath() {
        var repoPath = git.getRepository().getDirectory().getAbsolutePath();
        // substring without ".git"
        return repoPath.substring(0, repoPath.length() - 4);
    }

    public boolean checkoutToBranch(String branch) {
        try {
            git.checkout()
                .setName(branch)
                .setCreateBranch(true)
                .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                .setStartPoint("origin/" + branch)
                .setForced(true)
                .call();
            return true;
        } catch (RefNotFoundException e) {            // docs
            log.warn("Branch {} not found", branch);
            return false;
        } catch (RefAlreadyExistsException e) {
            log.error("Branch {} already exists", branch); // TODO может быть, сделать switch??
            return false;
        } catch (Exception e) {
            log.fatal("Unknown error: {}", e.getMessage());
            return false;
        }
    }

    private LocalDate getCommitLocalDate(RevCommit commit) {
        PersonIdent authorIdent = commit.getCommitterIdent();
        Date authorDate = authorIdent.getWhen();
        TimeZone authorTimeZone = authorIdent.getTimeZone();

        return authorDate.toInstant()
            .atZone(authorTimeZone.toZoneId())
            .toLocalDate();
    }

    public Stream<Commit> getCommitsStream(String folderPath) {
        try {
            return StreamSupport.stream(
                git.log()
                    .addPath(folderPath)
                    .call()
                    .spliterator(),
                false
            ).map(revCommit -> new Commit(revCommit.getShortMessage(), getCommitLocalDate(revCommit)));
        } catch (GitAPIException e) {
            log.fatal("{} on getCommitsStream: {}", folderPath, e.getMessage());
            return Stream.empty();
        }
    }

    public Stream<Commit> getCommitsStream() {
        try {
            return StreamSupport.stream(
                git.log()
                    .call()
                    .spliterator(),
                false
            ).map(revCommit -> new Commit(revCommit.getShortMessage(), getCommitLocalDate(revCommit)));
        } catch (GitAPIException e) {
            log.fatal("on getCommitsStream: {}", e.getMessage());
            return Stream.empty();
        }
    }

    public Optional<Commit> getLastCommit() {
        return getCommitsStream()
            .max(Comparator.comparing(Commit::date));
    }

    public Optional<Commit> getLastCommit(String folderPath) {
        return getCommitsStream(folderPath)
            .max(Comparator.comparing(Commit::date));
    }

    public Optional<Commit> getFirstCommit() {
        return getCommitsStream()
            .min(Comparator.comparing(Commit::date));
    }

    public Optional<Commit> getFirstCommit(String folderPath) {
        return getCommitsStream(folderPath)
            .min(Comparator.comparing(Commit::date));
    }

    public boolean isCommittedDuringPeriod(LocalDate after, LocalDate before) {
        return getCommitsStream()
            .map(Commit::date)
            .filter(commitDate -> commitDate.isAfter(after))
            .anyMatch(commitDate -> commitDate.isBefore(before));
    }

    public boolean isCommittedDuringWeek(LocalDate dayOnAWeek) {
        return isCommittedDuringPeriod(
            dayOnAWeek.minusDays(dayOnAWeek.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue() + 1),
            dayOnAWeek.plusDays(DayOfWeek.SUNDAY.getValue() - dayOnAWeek.getDayOfWeek().getValue() + 1)
        );
    }

    @Override
    public void close() throws Exception {
        git.close();
    }

    public record Commit(String shortMessage, LocalDate date) {
    }
}
