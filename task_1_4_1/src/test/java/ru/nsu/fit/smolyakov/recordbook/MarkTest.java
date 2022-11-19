package ru.nsu.fit.smolyakov.recordbook;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.smolyakov.recordbook.internal.Mark;

import java.util.OptionalInt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MarkTest {
    @Test
    void equalsTest() {
        Mark mark = Mark.of(Mark.Form.EXAM, "5");

        assertThat(mark).isEqualTo(mark);

        assertThat(mark.hashCode()).isEqualTo(Mark.of(Mark.Form.EXAM, "5").hashCode());
        assertThat(mark).isEqualTo(Mark.of(Mark.Form.EXAM, "5"));

        assertThat(mark).isNotEqualTo(Mark.of(Mark.Form.EXAM, "4"));
        assertThat(mark).isNotEqualTo(Mark.of(Mark.Form.DIFFERENTIATED_CREDIT, "5"));
        assertThat(mark).isNotEqualTo(Mark.of(Mark.Form.CREDIT, "pass"));

        assertThat(mark).isNotEqualTo(Integer.valueOf(5));
        assertThat(mark).isNotEqualTo(null);
    }

    @Test
    void examMarkTest() {
        Mark mark = Mark.of(Mark.Form.EXAM, "5");
        assertThat(mark.value()).isEqualTo(OptionalInt.of(5));
        assertThat(mark.isPass()).isTrue();

        assertThat(mark.isCredit()).isFalse();
        assertThat(mark.isDifferentiatedCredit()).isFalse();
        assertThat(mark.isExam()).isTrue();

        assertThat(mark.toString()).isEqualTo("5 (exam)");
    }

    @Test
    void differentiatedCreditMarkTest() {
        Mark mark = Mark.of(Mark.Form.DIFFERENTIATED_CREDIT, "2");
        assertThat(mark.value()).isEqualTo(OptionalInt.of(2));
        assertThat(mark.isPass()).isFalse();

        assertThat(mark.isCredit()).isFalse();
        assertThat(mark.isDifferentiatedCredit()).isTrue();
        assertThat(mark.isExam()).isFalse();

        assertThat(mark.toString()).isEqualTo("2 (diff. credit)");
    }

    @Test
    void creditMarkTest() {
        Mark mark = Mark.of(Mark.Form.CREDIT, "pass");
        assertThat(mark.value()).isEqualTo(OptionalInt.empty());
        assertThat(mark.isPass()).isTrue();

        assertThat(mark.isCredit()).isTrue();
        assertThat(mark.isDifferentiatedCredit()).isFalse();
        assertThat(mark.isExam()).isFalse();

        assertThat(mark.toString()).isEqualTo("pass (credit)");
    }

    @Test
    void negativeMarkTest() {
        Mark mark = Mark.of(Mark.Form.CREDIT, "fail");
        assertThat(mark.value()).isEqualTo(OptionalInt.empty());
        assertThat(mark.isPass()).isFalse();

        assertThat(mark.isCredit()).isTrue();
        assertThat(mark.isDifferentiatedCredit()).isFalse();
        assertThat(mark.isExam()).isFalse();

        assertThat(mark.toString()).isEqualTo("fail (credit)");
    }

    @Test
    void wrongValueTest() {
        assertThatThrownBy(() -> Mark.of(Mark.Form.CREDIT, "aboba"))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Mark.of(Mark.Form.EXAM, "666"))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Mark.of(Mark.Form.DIFFERENTIATED_CREDIT, "five"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
