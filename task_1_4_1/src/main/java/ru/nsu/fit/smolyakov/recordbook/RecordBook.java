package ru.nsu.fit.smolyakov.recordbook;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.fit.smolyakov.recordbook.internal.GradeBookDataParser;
import ru.nsu.fit.smolyakov.recordbook.internal.Mark;
import ru.nsu.fit.smolyakov.recordbook.internal.SemesterGradesTable;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * An immutable class which contains information about student's record book,
 * including {@link #getName()}, {@link #getUniversity()}, {@link #getRecordBookNumber()} and {@link #getSpeciality()}.
 * The main purpose of this class is to evaluate information about student's academic
 * performance ({@link ru.nsu.fit.smolyakov.recordbook.internal.GradeBookDataParser} is
 * responsible for this) and make some general conclusions about results of his study (e.g.
 * {@link #hasScholarshipNow()}, {@link #diplomaGradePointAverage()}, {@link #isRedDiplomaPossible()},
 * {@link #getCurrentSemesterNumber()}).
 *
 * <p>More local information about each semester grades or final grades may be recieved by
 * {@link SemesterGradesTable} methods, whose instance is returned by
 * respectively {@link getSemesterGradesTable} and {@link getFinalGradesTable}.
 *
 * @see ru.nsu.fit.smolyakov.recordbook.internal.GradeBookDataParser
 * @see ru.nsu.fit.smolyakov.recordbook.internal.Mark
 * @see ru.nsu.fit.smolyakov.recordbook.internal.SemesterGradesTable
 */
public class RecordBook extends GradeBookDataParser {
    private final String university;
    private final String name;
    private final int recordBookNumber;
    private final String speciality;

    @JsonCreator
    private RecordBook(
        @JsonProperty("university") String university,
        @JsonProperty("name") String name,
        @JsonProperty("recordBookNumber") int recordBookNumber,
        @JsonProperty("speciality") String speciality,
        @JsonProperty("currentSemesterNumber") int currentSemesterNumber,
        @JsonProperty("grades") List<SemesterGradesTable> grades) {
        super(currentSemesterNumber, grades);

        this.university = university;
        this.name = name;
        this.recordBookNumber = recordBookNumber;
        this.speciality = speciality;
    }

    /**
     * Returns an instance of {@linkplain RecordBook} corresponding to
     * JSON data supplied by {@code file}.
     *
     * @param file a JSON file with record book information
     * @return an instance of this class
     * @throws IllegalArgumentException if {@code file} contains contradictory
     *                                  fields
     * @throws IOException              if {@code file} can't be accessed
     * @throws StreamReadException      if error during parsing occured
     * @throws DatabindException        if error during parsing occured
     */
    public static RecordBook fromJson(File file) throws StreamReadException,
        DatabindException,
        IOException {
        return new ObjectMapper().readValue(file, RecordBook.class);
    }

    /**
     * Returns the name of university.
     *
     * @return the university
     */
    public String getUniversity() {
        return university;
    }

    /**
     * Returns the name of a student.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the serial number of this record book.
     *
     * @return the number of this record book
     */
    public int getRecordBookNumber() {
        return recordBookNumber;
    }

    /**
     * Returns the speciality of a student whose academic perfomance is reflected by
     * this record book.
     *
     * @return the speciality
     */
    public String getSpeciality() {
        return speciality;
    }

    /**
     * Returns if a student whose academic perfomance is reflected by
     * this record book is getting scholarship at current semester.
     *
     * <p>Indeed, the result is {@code true} if there weren't "fail",
     * 2 or 3 grades in the previous semester, or this semester is first.
     *
     * @return {@code true} if a student gets scholarship
     */
    public boolean hasScholarshipNow() {
        if (getCurrentSemesterNumber() == 1) {
            return true;
        } else {
            var previousSemesterGrades = getSemesterGradesTable(getCurrentSemesterNumber() - 1).get();

            return previousSemesterGrades.isPass()
                && previousSemesterGrades.amountOfGrades(3) == 0;
        }
    }

    /**
     * Returns if a student whose academic perfomance is reflected by
     * this record book is getting increased scholarship at current semester.
     *
     * <p>Indeed, the result is {@code true} if the only grades this student
     * achieved previous semester were fives and passes or this semester
     * is first.
     *
     * @return {@code true} if a student gets increased scholarship
     */
    public boolean hasIncreasedScholarshipNow() {
        if (getCurrentSemesterNumber() == 1) {
            return true;
        } else {
            var previousSemesterGrades = getSemesterGradesTable(getCurrentSemesterNumber() - 1).get();
            return previousSemesterGrades.amountOfGrades(5)
                + previousSemesterGrades.amountOfGrades(Mark.of(Mark.Form.CREDIT, "pass"))
                == previousSemesterGrades.amountOfGrades();
        }
    }

    /**
     * Returns the current grade point average of a diploma.
     *
     * @return current grade point average of a diploma
     */
    public double diplomaGradePointAverage() {
        return getFinalGradesTable().gradePointAverage();
    }

    /**
     * Returns if a student whose academic perfomance is reflected by
     * this record book is able to graduate with a red diploma.
     * It means that at least 75% of final grades are fives or "passes"
     * and all the rest are fours.
     *
     * @return {@code true} if red diploma is possible
     */
    public boolean isRedDiplomaPossible() {
        var finalGradesTable = getFinalGradesTable();

        return (double) finalGradesTable.amountOfGrades(5) / finalGradesTable.amountOfDifferentiatedGrades() >= 0.75
            && finalGradesTable.amountOfGrades(3) == 0
            && finalGradesTable.isPass();
    }

    /**
     * Writes a text representation of this {@code RecordBook} to a
     * specified {@code writer}.
     *
     * <p>A length of the subjects name is limited to 60 characters,
     * if one is bigger then its ending will be cut.
     *
     * @param writer a {@link PrintStream} to write the table to
     */
    public void toTextTable(PrintStream writer) {
        writer.printf("~~~ Record book no. %d ~~~\n", recordBookNumber);
        writer.printf("Name: %s\n", name);
        writer.printf("University: %s\n", university);
        writer.printf("Speciality: %s\n\n", speciality);

        writer.printf("~~~ Grade point average: %.1f ~~~\n", diplomaGradePointAverage());

        var diplomaGrades = getFinalGradesTable().grades().entrySet();
        if (diplomaGrades.isEmpty()) {
            return;
        }

        int maxLength = diplomaGrades
            .stream()
            .map((entry) -> entry.getKey().length())
            .max(Integer::compare)
            .get();

        int len = Integer.min(maxLength, 60);
        String format = "%%-%d.%ds | %%s\n".formatted(len, len);

        diplomaGrades.stream()
            .sorted(Comparator.comparing(Map.Entry::getKey))
            .forEachOrdered((entry) -> writer.printf(format, entry.getKey(), entry.getValue()));
    }
}
