package ru.nsu.fit.smolyakov.diary.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Predicate;

public class Diary {
    private final List<Note> notes
            = new ArrayList<Note>();

    // TODO [de]serialization

    @JsonCreator
    public Diary(@JsonProperty("notes") List<Note> notes) {
        this.notes.addAll(Objects.requireNonNull(notes));
    }

    public static Diary fromJson(File file) throws IOException {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(file, Diary.class);
    }

    public void toJson(File file) throws  IOException {
        new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValue(file, this);
    }


    // query builder
    public static class Query {
        private final List<Note> notes;
        private Predicate<Note> restrictions = ((note) -> true);
        private Query(List<Note> notes) {
            this.notes = notes;
        }

        public Query after(OffsetDateTime date) {
            restrictions = restrictions.and((note) -> note.after(date));
            return this;
        }

        public Query before(OffsetDateTime date) {
            restrictions = restrictions.and((note) -> note.before(date));
            return this;
        }

        public Query contains(String keyword) {
            restrictions = restrictions.and((note) -> note.contains(keyword));
            return this;
        }

        public Query contains(List<String> keywordsList) {
            keywordsList
                    .forEach((keyword) -> restrictions = restrictions.and((note) -> note.contains(keyword)));
            return this;
        }

        public Diary select() {
            return new Diary(
                notes.stream()
                        .filter(restrictions)
                        .sorted(Comparator.comparing(Note::date))
                        .toList() // TODO maybe there is a better way
            );
        }
    }

    public Query query() {
        return new Query(this.notes);
    }

    public boolean remove(String heading) {
        return notes.removeIf((note) -> note.heading().equals(heading));
    }

    public boolean insert(String heading, String contents) {
        return notes.add(Note.create(heading, contents));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        notes.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }
}
