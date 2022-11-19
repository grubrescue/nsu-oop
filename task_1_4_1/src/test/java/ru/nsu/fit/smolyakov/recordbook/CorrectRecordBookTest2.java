package ru.nsu.fit.smolyakov.recordbook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class CorrectRecordBookTest2 {
    RecordBook myBook;

    @BeforeEach
    void init() throws Exception {
        File file = new File("src/test/resources/ru/nsu/fit/smolyakov/recordbook/CorrectBook2.json");
        myBook = RecordBook.fromJson(file);
    }

    @Test
    void correctRecordBookTest2() {
        assertThat(myBook.getCurrentSemesterNumber()).isEqualTo(3);

        assertThat(myBook.isRedDiplomaPossible()).isTrue();
        assertThat(myBook.hasScholarshipNow()).isTrue();
        assertThat(myBook.hasIncreasedScholarshipNow()).isTrue();
        assertThat(myBook.diplomaGradePointAverage()).isEqualTo(4.888888888888889);
    }
}
