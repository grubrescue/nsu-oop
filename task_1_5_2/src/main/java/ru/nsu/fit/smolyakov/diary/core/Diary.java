package ru.nsu.fit.smolyakov.diary.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 *
 */
public class Diary {
    private final static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setVisibility(
                PropertyAccessor.FIELD,
                JsonAutoDetect.Visibility.ANY
            );
    }

    private final List<Note> notes
        = new ArrayList<Note>();

    @JsonCreator
    public Diary() {
    }

    /**
     * @param notes
     */
    @JsonCreator
    public Diary(@JsonProperty("notes") List<Note> notes) {
        this.notes.addAll(Objects.requireNonNull(notes));
    }

    /**
     * @param file
     * @return
     * @throws IOException
     */
    public static Diary fromJson(File file) throws IOException {
        return mapper.readValue(file, Diary.class);
    }

    /**
     * @param file
     * @throws IOException
     */
    public void toJson(File file) throws IOException {
        mapper.writeValue(file, this);
    }

    public Query query() {
        return new Query(this.notes);
    }

    public boolean remove(String heading) {
        return notes.removeIf((note) -> note.heading().equals(heading));
    }

    public void insert(String heading, String contents) {
        notes.add(Note.create(heading, contents));
    }

    @Override
    public int hashCode() {
        return notes.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof Diary other) {
            return notes.equals(other.notes);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        var stringBuilder = new StringBuilder();
        notes.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

    /**
     *
     */
    public static class Query {
        private final List<Note> notes;
        private Predicate<Note> hasKeywords = ((note) -> false);
        private ZonedDateTime after =
            Instant.ofEpochMilli(Long.MIN_VALUE).atZone(ZoneOffset.UTC);
        private ZonedDateTime before =
            Instant.ofEpochMilli(Long.MAX_VALUE).atZone(ZoneOffset.UTC);

        private Query(List<Note> notes) {
            this.notes = Objects.requireNonNull(notes);
        }

        public Query after(ZonedDateTime date) {
            if (date != null && date.isAfter(this.after)) {
                after = date;
            }
            return this;
        }

        public Query before(ZonedDateTime date) {
            if (date != null && date.isBefore(this.before)) {
                before = date;
            }
            return this;
        }

        public Query contains(String keyword) {
            if (keyword != null) {
                hasKeywords = hasKeywords.or((note) -> note.contains(keyword));
            }
            return this;
        }

        public Query contains(List<String> keywordsList) {
            if (keywordsList != null) {
                keywordsList
                    .forEach((keyword) ->
                        hasKeywords =
                            hasKeywords.or(
                                (note) -> note.contains(keyword)
                            )
                    );
            }
            return this;
        }

        public Diary select() {
            return new Diary(
                notes.stream()
                    .filter(hasKeywords)
                    .filter((entry) -> entry.date().isBefore(before))
                    .filter((entry) -> entry.date().isAfter(after))
                    .sorted(Comparator.comparing(Note::date))
                    .toList() // TODO maybe there is a better way
            );
        }
    }
}
