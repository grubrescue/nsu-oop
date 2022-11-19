package ru.nsu.fit.smolyakov.recordbook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.recordbook.internal.Mark;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CorrectRecordBookTest {
    RecordBook myBook;

    @BeforeEach
    void init() throws Exception {
        File file = new File("src/test/resources/ru/nsu/fit/smolyakov/recordbook/CorrectBook.json");
        myBook = RecordBook.fromJson(file);
    }

    @Test
    void correctRecordBookTest() {
        assertThat(myBook.getUniversity()).isEqualTo("Novosibirsk State University");
        assertThat(myBook.getName()).isEqualTo("Smolyakov Pavel Evgenievich");
        assertThat(myBook.getRecordBookNumber()).isEqualTo(210634);
        assertThat(myBook.getSpeciality()).isEqualTo("Информатика и вычислительная техника");

        assertThat(myBook.getCurrentSemesterNumber()).isEqualTo(3);

        assertThat(myBook.isRedDiplomaPossible()).isFalse();
        assertThat(myBook.hasScholarshipNow()).isFalse();
        assertThat(myBook.hasIncreasedScholarshipNow()).isFalse();
        assertThat(myBook.diplomaGradePointAverage()).isEqualTo(4.25);

        assertThat(myBook.getFinalGradesTable().getSubjectMark("История"))
            .isEqualTo(Optional.of(Mark.of(Mark.Form.DIFFERENTIATED_CREDIT, "5")));
        assertThat(myBook.getFinalGradesTable().getSubjectMark("Исторя"))
            .isEqualTo(Optional.empty());
    }

    @Test
    void textOutputTest() {
        var expected = """
            ~~~ Record book no. 210634 ~~~
            Name: Smolyakov Pavel Evgenievich
            University: Novosibirsk State University
            Speciality: Информатика и вычислительная техника
                    
            ~~~ Grade point average: 4.3 ~~~
            Введение в алгебру и анализ                              | 4 (exam)
            Введение в дискретную математику и математическую логику | 3 (exam)
            Декларативное программирование                           | 5 (diff. credit)
            Измерительный практикум                                  | pass (credit)
            Императивное программирование                            | 5 (exam)
            Иностранный язык                                         | 3 (diff. credit)
            История                                                  | 5 (diff. credit)
            Основы культуры речи                                     | 5 (diff. credit)
            Физическая культура и спорт                              | pass (credit)
            Цифровые платформы                                       | 4 (diff. credit)
            """;

        OutputStream outputStream = new ByteArrayOutputStream();
        myBook.toTextTable(new PrintStream(outputStream));

        assertThat(outputStream.toString()).isEqualTo(expected);

    }
}
