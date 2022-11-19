package ru.nsu.fit.smolyakov.recordbook.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An immutable class which takes the responsibility of storing data of all the grades of a
 * specified semester. May be instatiated only by {@link GradeBookDataParser}, and,
 * accordingly, {@link ru.nsu.fit.smolyakov.recordbook.RecordBook}.
 *
 * <p>Able to return current {@link semesterNumber}, string representation by {@link toString} and
 * calculate {@link gradePointAverage}. The copy of whole grades table may be instantiated by
 * {@link grades}. {@link #amountOfGrades()} returns a total amount of grades, and
 * {@link #amountOfGrades(Mark)} and {@link #amountOfGrades(int)} returns an amount of grades equal
 * to specified {@link Mark} and amount of grades with specified valuation (diff. credits
 * and exams only) respectively.
 */
public class SemesterGradesTable {
    private final int semesterNumber;
    private Map<Mark, Integer> eachMarkCount = new HashMap<>();
    private Map<String, Mark> grades = new HashMap<>();

    SemesterGradesTable(int semesterNumber) {
        this.semesterNumber = semesterNumber;
    }

    @JsonCreator
    SemesterGradesTable(
        @JsonProperty("semesterNumber") int semesterNumber,
        @JsonProperty("subjects") Map<String, Mark> map) {
        this(semesterNumber);
        putAll(map);
    }

    private boolean put(String subject, Mark mark) {
        if (!grades.containsKey(subject)) {
            eachMarkCount.put(mark, eachMarkCount.getOrDefault(mark, 0) + 1);
            grades.put(subject, mark);
            return true;
        } else {
            return false;
        }
    }

    void putAll(SemesterGradesTable gradesTable) {
        gradesTable.grades.forEach(this::put);
    }

    private void putAll(Map<String, Mark> map) {
        map.forEach(this::put);
    }

    /**
     * Returns the number of semester to which this {@code SemesterGradesTable}
     * is related.
     *
     * <p>If this table is one with final grades, returns current semester number
     * (determined by {@link GradeBookDataParser}).
     *
     * @return the semesterNumber
     * @see GradeBookDataParser
     * @see ru.nsu.fit.smolyakov.recordbook.RecordBook
     */
    public int getSemesterNumber() {
        return semesterNumber;
    }

    /**
     * Returns an amount of grades in this semester.
     *
     * @return an amount of grades
     */
    public int amountOfGrades() {
        return grades.size();
    }

    /**
     * Returns an amount of grades equal to a specified mark.
     *
     * @param mark a specified mark
     * @return an amount of grades equal to a specified mark
     */
    public int amountOfGrades(Mark mark) {
        return eachMarkCount.getOrDefault(mark, 0);
    }

    /**
     * Returns an amount of grades equal to a specified valuation
     * (both differentiated credits and exams).
     *
     * @param value a specified valuation of a mark
     * @return an amount of grades equal to a specified valuation
     */
    public int amountOfGrades(int value) {
        // System.out.println(value + "\n" +
        //     eachMarkCount
        //     .size()
        // );

        return (int) eachMarkCount
            .entrySet()
            .stream()
            .filter((grade) -> !grade.getKey().isCredit())
            .filter((grade) -> grade.getKey().value().getAsInt() == value)
            .map((grade) -> grade.getValue())
            .reduce(0, Integer::sum);
    }

    /**
     * Returns if there weren't any negative grades in this semester.
     *
     * @return {@code true} if there weren't any negative grades in
     * this semester
     */
    public boolean isPass() {
        return amountOfGrades(Mark.of(Mark.Form.CREDIT, "fail")) == 0
            && amountOfGrades(2) == 0;
    }

    /**
     * Returns a map containing each subject reflected to
     * its {@link ru.nsu.fit.smolyakov.recordbook.internal.Mark}.
     *
     * @return a map containing all subjects and grades for them
     */
    public Map<String, Mark> grades() {
        return Map.copyOf(grades);
    }

    /**
     * Returns grade point average for this semester.
     *
     * @return grade point average for this semester.
     */
    public double gradePointAverage() {
        var gradesSum = eachMarkCount
            .entrySet()
            .stream()
            .filter((grade) -> !grade.getKey().isCredit())
            .reduce(0,
                (acc, grade) -> acc + grade.getKey().value().getAsInt() * grade.getValue(),
                (t1, t2) -> t1 + t2
            );

        var amountOfDifferentiatedGrades =
            amountOfGrades()
                - amountOfGrades(Mark.of(Mark.Form.CREDIT, "fail"))
                - amountOfGrades(Mark.of(Mark.Form.CREDIT, "pass"));

        return (double) gradesSum / amountOfDifferentiatedGrades;
    }

    /**
     * Returns an amount of differentiated grades.
     *
     * @return an amount of differentiated grades
     */
    public int amountOfDifferentiatedGrades() {
        return amountOfGrades()
            - amountOfGrades(Mark.of(Mark.Form.CREDIT, "fail"))
            - amountOfGrades(Mark.of(Mark.Form.CREDIT, "pass"));
    }

    /**
     * Returns a string representation of this semester grades,
     * namely a number of semester, each subject and mark for it.
     *
     * @return a string representation of this semester grades
     */
    @Override
    public String toString() {
        return "semesterNumber = " + semesterNumber + ";\n"
            + grades + ".";
    }

    /**
     * Returns a {@link Mark} for a subject specified by
     * {@code subjectName}.
     *
     * @param subjectName a name of a subject
     * @return a mark
     */
    public Optional<Mark> getSubjectMark(String subjectName) {
        return Optional.ofNullable(grades.get(subjectName));
    }
}
