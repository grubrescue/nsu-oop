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

/**
 * A class to work with student's repository.
 *
 * <p>Uses JGit library as a backend.
 */
@Log4j2
public class StudentRepository implements AutoCloseable {
    private final Git git;

    /**
     * Creates an instance of {@link StudentRepository} and clones repository from given URI.
     *
     * @param uri       repository URI
     *                  (e.g. {@code https://github.com/evangelionexpert/oop.git}). Supposed to be valid.
     * @param directory directory to clone repository to
     * @throws GitAPIException if an error occurs during cloning
     */
    public StudentRepository(String uri, File directory) throws GitAPIException {
        git = Git.cloneRepository()
            .setURI(uri)
            .setDirectory(directory)
            .setCloneAllBranches(true)
            .call();
    }

    /**
     * Returns the absolute path to the repository directory.
     *
     * @return absolute path to the repository directory
     */
    public String getAbsolutePath() {
        var repoPath = git.getRepository().getDirectory().getAbsolutePath();
        // substring without ".git"
        return repoPath.substring(0, repoPath.length() - 4);
    }

    /**
     * Checkouts to the latest commit on the given branch.
     *
     * @param branch branch name
     * @return true on success, false otherwise
     */
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

    /**
     * Returns a stream of commits on the given folder.
     *
     * @param folderPath path to the folder
     * @return stream of commits; empty stream if an error occurs
     */
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

    /**
     * Returns a stream of all commits in the repository.
     *
     * @return stream of commits; empty stream if an error occurs
     */
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

    /**
     * Returns a last commit in the repository.
     *
     * @return an {@link Optional} of the last commit;
     * {@link Optional#empty()} if there are no commits
     * or an error occurs
     */
    public Optional<Commit> getLastCommit() {
        return getCommitsStream()
            .max(Comparator.comparing(Commit::date));
    }

    /**
     * Returns a last commit in the given folder.
     *
     * @param folderPath path to the folder
     * @return an {@link Optional} of the last commit;
     * {@link Optional#empty()} if there are no commits
     * or an error occurs
     */
    public Optional<Commit> getLastCommit(String folderPath) {
        return getCommitsStream(folderPath)
            .max(Comparator.comparing(Commit::date));
    }

    /**
     * Returns a first commit in the repository.
     *
     * @return an {@link Optional} of the first commit;
     * {@link Optional#empty()} if there are no commits
     * or an error occurs
     */
    public Optional<Commit> getFirstCommit() {
        return getCommitsStream()
            .min(Comparator.comparing(Commit::date));
    }

    /**
     * Returns a first commit in the given folder.
     *
     * @param folderPath path to the folder
     * @return an {@link Optional} of the first commit;
     * {@link Optional#empty()} if there are no commits
     * or an error occurs
     */
    public Optional<Commit> getFirstCommit(String folderPath) {
        return getCommitsStream(folderPath)
            .min(Comparator.comparing(Commit::date));
    }

    /**
     * Returns if there were any commits during the given period.
     *
     * @param after  the start of the period
     * @param before the end of the period
     * @return true if there were any commits during the given period, false otherwise
     */
    public boolean isCommittedDuringPeriod(LocalDate after, LocalDate before) {
        return getCommitsStream()
            .map(Commit::date)
            .filter(commitDate -> commitDate.isAfter(after))
            .anyMatch(commitDate -> commitDate.isBefore(before));
    }

    /**
     * Returns if there were any commits during the given week.
     *
     * @param dayOnAWeek a day of the week
     * @return true if there were any commits during the given week, false otherwise
     */
    public boolean isCommittedDuringWeek(LocalDate dayOnAWeek) {
        return isCommittedDuringPeriod(
            dayOnAWeek.minusDays(dayOnAWeek.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue() + 1),
            dayOnAWeek.plusDays(DayOfWeek.SUNDAY.getValue() - dayOnAWeek.getDayOfWeek().getValue() + 1)
        );
    }

    /**
     * Closes the repository.
     *
     * @throws Exception if an error occurs
     * @see org.eclipse.jgit.api.Git#close()
     * @see AutoCloseable
     */
    @Override
    public void close() throws Exception {
        git.close();
    }

    /**
     * A representation of a commit.
     *
     * @param shortMessage short commit message
     * @param date         commit date
     */
    public record Commit(String shortMessage, LocalDate date) {
    }
}
