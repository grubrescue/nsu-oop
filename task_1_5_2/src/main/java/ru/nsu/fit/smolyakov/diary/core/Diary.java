package ru.nsu.fit.smolyakov.diary.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A class providing some basic interface to operate with a diary, stored in
 * an external Json-file (see {@link #toJson(File)}, {@link #fromJson(File)}.
 *
 * <p>Allows to {@link #insert(String, String)} a new {@link Note} with a specified heading and
 * text and an actual date of creation, {@link #remove(String)} notes with a specified
 * heading, get pretty representation of all entries via {@link #toString()}.
 *
 * <p>{@link Query} nested class gives a functionality to filter notes that belong to
 * this {@code Diary}.
 *
 * @see Note
 */
public class Diary {
    private final static ObjectMapper mapper;
    private final static ObjectWriter writer;

    static {
        mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setVisibility(
                PropertyAccessor.FIELD,
                JsonAutoDetect.Visibility.ANY
            )
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);


        writer = mapper
            .writer()
            .withDefaultPrettyPrinter();
    }

    private final List<Note> notes
        = new ArrayList<Note>();

    /**
     * Constructs an empty {@code Diary}.
     */
    @JsonCreator
    public Diary() {
    }

    @JsonCreator
    private Diary(@JsonProperty("notes") List<Note> notes) {
        this.notes.addAll(Objects.requireNonNull(notes));
    }

    /**
     * Constructs a {@code Diary} instance according to the data
     * specified by a specified Json-formatted {@code file}.
     *
     * @param file a Json-file to deserialize
     * @return a {@code Diary} instance
     * @throws IOException if {@code file} doesn't exist, unavailable
     *                     or has an incorrect format
     */
    public static Diary fromJson(File file) throws IOException {
        return mapper.readValue(file, Diary.class);
    }

    /**
     * Serializes this {@code Diary} instance to a specified
     * Json-formatted {@code file}.
     *
     * @param file a Json-file to serialize to
     * @throws IOException if {@code file} doesn't exist or
     *                     unavailable
     */
    public void toJson(File file) throws IOException {
        writer.writeValue(file, this);
    }

    /**
     * Returns a new {@link Query} instance associated with this {@code Diary}.
     *
     * @return a new {@link Query} instance.
     */
    public Query query() {
        return new Query(this.notes);
    }

    /**
     * Removes all notes that match a specified {@code heading}.
     *
     * @param heading a title
     * @return {@code true} if at least one note was removed
     */
    public boolean remove(String heading) {
        return notes.removeIf((note) -> note.heading().equals(heading));
    }

    /**
     * Creates a new {@code Note} and inserts it in this {@code Diary}.
     *
     * @param heading  title
     * @param contents text
     */
    public void insert(String heading, String contents) {
        notes.add(Note.create(heading, contents));
    }

    /**
     * Returns an amount of notes in this {@code Diary}.
     *
     * @return an amount of notes
     */
    public int count() {
        return notes.size();
    }

    /**
     * Returns a pretty {@link String} representation of all entries of this {@code Diary}.
     * Depends on a {@link Note#toString()} method.
     *
     * @return a string representation of this {@code Diary}.
     */
    @Override
    public String toString() {
        var stringBuilder = new StringBuilder();
        notes.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

    /**
     * Gives a functionality to filter notes that belong to the outer {@code Diary}.
     * Time of creation restrictions are provided by {@link #after(ZonedDateTime)} and
     * {@link #before(ZonedDateTime)}, and checks for keywords being contained in
     * a note title are done by {@link #containsInHeading(String)} and {@link #containsInHeading(List)}.
     *
     * <p>Can be instantiated only by {@link #query()} method of the outer class.
     */
    public static class Query {
        private final List<Note> notes;
        private List<String> keywordsList = new ArrayList<>();
        private ZonedDateTime after =
            Instant.EPOCH.atZone(ZoneOffset.UTC);
        private ZonedDateTime before =
            Instant.ofEpochMilli(Long.MAX_VALUE).atZone(ZoneOffset.UTC);

        private Query(List<Note> notes) {
            this.notes = Objects.requireNonNull(notes);
        }

        /**
         * Filters out notes that are created after a specified {@code date}
         * and returns this instance.
         *
         * @param date a specified date
         * @return this
         */
        public Query after(ZonedDateTime date) {
            if (date != null && date.isAfter(this.after)) {
                after = date;
            }
            return this;
        }

        /**
         * Filters out notes that are created before a specified {@code date}
         * and returns this instance.
         *
         * @param date a specified date
         * @return this
         */
        public Query before(ZonedDateTime date) {
            if (date != null && date.isBefore(this.before)) {
                before = date;
            }
            return this;
        }

        /**
         * Filters out notes that doesn't contain a specified {@code keyword}
         * or one of other that had been specified before and returns this instance.
         *
         * @param keyword a specified keyword
         * @return this
         */
        public Query containsInHeading(String keyword) {
            if (keywordsList != null) {
                this.keywordsList.add(keyword);
            }
            return this;
        }

        /**
         * Filters out notes that doesn't contain at least one keyword from specified {@code keyword}
         * or one of other that had been specified before and returns this instance.
         *
         * @param keywordsList a specified keyword
         * @return this
         */
        public Query containsInHeading(List<String> keywordsList) {
            if (keywordsList != null) {
                this.keywordsList.addAll(keywordsList);
            }
            return this;
        }

        /**
         * Processes the query and returns a new instance of a {@link Diary}.
         *
         * @return the processed instance
         */
        public Diary select() {
            Predicate<Note> keywordsFilter =
                (note) -> keywordsList.isEmpty() || (keywordsList.stream().anyMatch(note::containsInHeading));

            return new Diary(
                notes.stream()
                    .filter(keywordsFilter)
                    .filter((entry) -> entry.date().isBefore(before))
                    .filter((entry) -> entry.date().isAfter(after))
                    .sorted(Comparator.comparing(Note::date))
                    .toList()
            );
        }
    }
}
