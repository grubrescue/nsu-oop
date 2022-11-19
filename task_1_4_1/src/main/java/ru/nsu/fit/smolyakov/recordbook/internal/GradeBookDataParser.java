package ru.nsu.fit.smolyakov.recordbook.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * An immutable abstract class which takes the responsibility of parsing the most important
 * record book data from Json (e.g. {@link SemesterGradesTable} of all semesters)
 * and storing them in an approptiate way.
 *
 * <p>Designed for an internal use with a main {@link ru.nsu.fit.smolyakov.recordbook.RecordBook}
 * class, so provides a limited functionality. Namely, it's a protected
 * {@link GradeBookDataParser} constructor which arranges the parsed data,
 * a {@link currentSemesterNumber} method returning an obvious value,
 * and {@link allSemestersGrades} and {@link finalGradesTable} each returning
 * a corresponding instance of {@link SemesterGradesTable}.
 *
 * @see SemesterGradesTable
 * @see ru.nsu.fit.smolyakov.recordbook.RecordBook
 */
public abstract class GradeBookDataParser {
    private final int currentSemesterNumber;

    private Map<Integer, SemesterGradesTable> allSemestersGrades;
    private SemesterGradesTable finalGradesTable;

    /**
     * Does all the job structuring the information about academic performance
     * during all semesters.
     *
     * <p>Propertied as ready to be parsed by Jackson.
     *
     * @param currentSemesterNumber a number of current semester
     * @param gradesList            a list of {@link SemesterGradesTable} instances
     * @throws IllegalArgumentException if {@code gradesList} is empty,
     *                                  some of {@code gradesList} tables includes contradictory
     *                                  number of semester, or the total amount of tables in
     *                                  {@code gradesList} is not equal to {@code currentSemesterNumber},
     *                                  which also must be more than one
     */
    @JsonCreator
    protected GradeBookDataParser(
        @JsonProperty("currentSemesterNumber") int currentSemesterNumber,
        @JsonProperty("grades") List<SemesterGradesTable> gradesList) {
        if (currentSemesterNumber < 1) {
            throw new IllegalArgumentException();
        } else if (gradesList == null) {
            throw new IllegalArgumentException();
        } else if (gradesList.size() != currentSemesterNumber - 1) {
            throw new IllegalArgumentException();
        }

        this.currentSemesterNumber = currentSemesterNumber;

        evaluateSemesterGrades(gradesList);
        evaluateFinalGradesTable();
    }

    private void evaluateSemesterGrades(List<SemesterGradesTable> from) {
        int realCount = (int) from.stream()
            .map(SemesterGradesTable::getSemesterNumber)
            .filter((x) -> (x > 0 && x < currentSemesterNumber))
            .distinct()
            .count();

        if (realCount != currentSemesterNumber - 1) {
            throw new IllegalArgumentException();
        }

        allSemestersGrades = new TreeMap<>();
        from.stream()
            .forEach((table) -> allSemestersGrades.put(table.getSemesterNumber(), table));
    }

    private void evaluateFinalGradesTable() {
        finalGradesTable = new SemesterGradesTable(currentSemesterNumber);
        for (int i = currentSemesterNumber - 1; i > 0; --i) {
            finalGradesTable.putAll(allSemestersGrades.get(i));
        }
    }

    /**
     * Returns the current semester number.
     *
     * @return the current semester number
     */
    public int getCurrentSemesterNumber() {
        return currentSemesterNumber;
    }

    /**
     * Returns the specified semester grades table.
     *
     * @param i number of semester
     * @return the grades of a semester specified by {@code i}.
     */
    public Optional<SemesterGradesTable> getSemesterGradesTable(int i) {
        return Optional.ofNullable(allSemestersGrades.get(i));
    }

    /**
     * Returns the final grades.
     *
     * @return the final grades
     */
    public SemesterGradesTable getFinalGradesTable() {
        return finalGradesTable;
    }
}
