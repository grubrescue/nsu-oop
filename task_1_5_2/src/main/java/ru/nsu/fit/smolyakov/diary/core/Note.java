package ru.nsu.fit.smolyakov.diary.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A note class providing a basic functionality to handle small writings with
 * a specified {@link #heading()} and {@link #text()}. Designed to be instantiated
 * and usually instantiated internally by {@link Diary}.
 *
 * <p>The contract consists of filtering predicates, such as {@link #contains(String)},
 * {@link #after(ZonedDateTime)}and {@link #before(ZonedDateTime)}, and {@link #toString()},
 * representing this note acceptably for a {@link Diary}.
 *
 * @param heading a title
 * @param text    contents
 * @param date    a date of creation
 * @see Diary
 * @see ZonedDateTime
 */
public record Note(
    @JsonProperty("heading")
    String heading,

    @JsonProperty("text")
    String text,

    @JsonProperty("date")
    @JsonFormat(
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    )
    ZonedDateTime date
) {
    /**
     * Creates a new instance of {@code Note} with a specified
     * {@code heading} and {@code contents} and current date of creation
     * (latter is provided by {@link ZonedDateTime#now()}).
     *
     * @param heading  a string literal that specifies a heading of an entry
     * @param contents a string literal that specifies a text of an entry
     * @return a new instance of {@code Note}
     */
    public static Note create(String heading, String contents) {
        return new Note(heading, contents, ZonedDateTime.now(Clock.systemDefaultZone()));
    }

    /**
     * Returns {@code true} if this {@code Note} heading contains
     * a specified {@code keyword}. Keyword is treated as a sequence of
     * characters, so this method doesn't search for words but for
     * the occurrence of one.
     *
     * @param keyword a string to be found in the heading of this {@code Note}
     * @return {@code true} if this {@code Note} heading contains
     * a specified {@code keyword}.
     */
    public boolean contains(String keyword) { // TODO maybe rename maybe not
        return heading.contains(keyword);
    }

    /**
     * Returns {@code true} if this {@code Note} was created
     * after a specified {@code date}.
     *
     * @param date a specified {@link ZonedDateTime}
     * @return {@code true} if this {@code Note} was created
     * after a specified {@code date}.
     */
    public boolean after(ZonedDateTime date) {
        return this.date.isAfter(date);
    }

    /**
     * Returns {@code true} if this {@code Note} was created
     * before a specified {@code date}.
     *
     * @param date a specified {@link ZonedDateTime}
     * @return {@code true} if this {@code Note} was created
     * before a specified {@code date}.
     */
    public boolean before(ZonedDateTime date) {
        return this.date.isBefore(date);
    }

    /**
     * Returns a pretty representation of this note.
     * An example of a format is presented below.
     * <code>
     * <br/>Heading: title
     * <br/>Text: contents
     * <br/>------------
     * <br/>Date: Tue, 27 Dec 2022 22:21:52 +0700
     * </code>
     *
     * @return a pretty representation of this note
     */
    @Override
    public String toString() {
        return """
            Heading: %s
            Text: %s
            ------------
            Date: %s
                            
            """
            .formatted(
                heading,
                text,
                date.format(DateTimeFormatter.RFC_1123_DATE_TIME)
            );
    }
}
