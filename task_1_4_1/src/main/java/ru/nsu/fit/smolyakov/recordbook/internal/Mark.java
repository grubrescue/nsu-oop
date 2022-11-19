package ru.nsu.fit.smolyakov.recordbook.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.OptionalInt;

/**
 * Provides a functionality to operate with marks specified
 * in input JSON.
 *
 * <p>Implements a {@link Mark} constructor which takes {@link Mark.Form}
 * of attestation and a value in range of 2 and 5 for {@link Mark.Form#EXAM}
 * and {@link Mark.Form#DIFFERENTIATED_CREDIT} or {@code "pass"} or
 * {@code "fail"} string literal for the {@link Mark.Form#CREDIT}, as well
 * as static {@link of} method to construct an instance.
 *
 * <p>Credits are not treated as valueable, so {@link value} method
 * returns {@link OptionalInt#empty} for them.
 *
 * @see SemesterGradesTable
 * @see ru.nsu.fit.smolyakov.recordbook.RecordBook
 */
public class Mark {
    private static final String PASS_STRING = "pass";
    private static final String FAIL_STRING = "fail";
    private static final int MIN_MARK = 2;
    private static final int MAX_MARK = 5;
    private int value;
    private Form attestationForm;
    /**
     * Constructs an instance of {@code Mark} class based on
     * a specified {@code attestationForm} and {@code markField}.
     * {@code markField} is supposed to be a value in range of 2 and 5
     * for {@link Mark.Form#EXAM} and {@link Mark.Form#DIFFERENTIATED_CREDIT}
     * or {@code "pass"} or {@code "fail"} string literal for the
     * {@link Mark.Form#CREDIT}, otherwise this method throws
     * an {@link IllegalArgumentException}.
     *
     * @param attestationForm a specified attestation form
     * @param markField       a specified mark
     * @throws IllegalArgumentException if {@code markField} is incorrect
     */
    @JsonCreator
    public Mark(@JsonProperty("attestationForm") Form attestationForm,
                @JsonProperty("mark") String markField) {
        this.attestationForm = attestationForm;

        if (attestationForm.equals(Form.CREDIT)) {
            value = switch (markField) {
                case PASS_STRING -> MAX_MARK;
                case FAIL_STRING -> MIN_MARK;
                default -> throw new IllegalArgumentException(
                    "expected \"pass\", or \"fail\""
                );
            };
        } else {
            int tmp;
            try {
                tmp = Integer.parseInt(markField);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                    "expected an integer from 2 to 5"
                );
            }

            if (tmp < MIN_MARK || tmp > MAX_MARK) {
                throw new IllegalArgumentException(
                    "expected an integer from 2 to 5"
                );
            } else {
                value = tmp;
            }
        }
    }

    /**
     * Returns an instance of {@code Mark} class based on
     * a specified {@code attestationForm} and {@code markField}.
     * {@code markField} is supposed to be a value in range of 2 and 5
     * for {@link Mark.Form#EXAM} and {@link Mark.Form#DIFFERENTIATED_CREDIT}
     * or {@code "pass"} or {@code "fail"} string literal for the
     * {@link Mark.Form#CREDIT}, otherwise this method throws
     * an {@link IllegalArgumentException}.
     *
     * @param attestationForm a specified attestation form
     * @param markField       a specified mark
     * @return an instance of {@code Mark}
     * @throws IllegalArgumentException if {@code markField} is incorrect
     */
    @JsonCreator
    public static Mark of(@JsonProperty("attestationForm") Form attestationForm,
                          @JsonProperty("mark") String markField) {
        return new Mark(attestationForm, markField);
    }

    /**
     * Returns a numeric representation of this mark.
     *
     * @return an {@linkplain OptionalInt} of numeric representation
     * of this mark if one exists,
     * {@linkplain OptionalInt#empty} otherwise
     */
    public OptionalInt value() {
        if (attestationForm.equals(Form.CREDIT)) {
            return OptionalInt.empty();
        } else {
            return OptionalInt.of(value);
        }
    }

    /**
     * Returns if the mark may be treated as pass.
     *
     * @return a numeric representation of this mark
     */
    public boolean isPass() {
        return value > 2;
    }

    /**
     * Returns if this mark is a credit.
     *
     * @return {@code true} if this mark is a credit
     */
    public boolean isCredit() {
        return attestationForm.equals(Form.CREDIT);
    }

    /**
     * Returns if this mark is a differentiated credit.
     *
     * @return {@code true} if this mark is a differentiated
     * credit
     */
    public boolean isDifferentiatedCredit() {
        return attestationForm.equals(Form.DIFFERENTIATED_CREDIT);
    }

    /**
     * Returns if this mark is an exam.
     *
     * @return {@code true} if this mark is an exam
     */
    public boolean isExam() {
        return attestationForm.equals(Form.EXAM);
    }

    /**
     * Returns a string representation of this mark,
     * namely its value and an attestation form.
     *
     * @return a string representation of this mark
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        if (attestationForm.equals(Form.CREDIT) && isPass()) {
            str.append("pass");
        } else if (attestationForm.equals(Form.CREDIT)) {
            str.append("fail");
        } else {
            str.append(value);
        }

        str.append(" (");
        str.append(attestationForm.caption());

        str.append(")");
        return str.toString();
    }

    /**
     * Returns a hash code for this {@link Mark}.
     *
     * @return a hash code for this {@link Mark}
     */
    @Override
    public int hashCode() {
        return Objects.hash(value, attestationForm);
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is an {@code Mark} object that
     * contains the same numeric representation of mark
     * as this object.
     *
     * @param obj the object to compare with.
     * @return {@code true} if the objects are the same;
     * {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (obj instanceof Mark m) {
            if (this.attestationForm == m.attestationForm) {
                return this.value == m.value;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Defines a form of attestation.
     */
    public enum Form {
        /**
         * A credit, expects "pass" or "fail".
         */
        @JsonProperty("credit") CREDIT("credit"),

        /**
         * A differentiated credit, expects a value in range of 2 and 5.
         */
        @JsonProperty("differentiatedCredit") DIFFERENTIATED_CREDIT("diff. credit"),

        /**
         * An exam, expects a value in range of 2 and 5.
         */
        @JsonProperty("exam") EXAM("exam");


        private String caption;

        Form(String caption) {
            this.caption = caption;
        }

        /**
         * Returns more pleasant representation of this enum.
         * {@link #CREDIT} is converted into "credit",
         * {@link #EXAM} is converted into "exam" and
         * {@link #DIFFERENTIATED_CREDIT} - into "diff. credit".
         *
         * @return more pleasant representation of this enum
         */
        public String caption() {
            return caption;
        }
    }
}
