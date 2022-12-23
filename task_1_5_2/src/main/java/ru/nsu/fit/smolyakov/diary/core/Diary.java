package ru.nsu.fit.smolyakov.diary.core;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Diary {
    private final List<Entry> entries
            = new ArrayList<Entry>(); // TODO maybe arraylist will suit better?

    // TODO [de]serialization
    public Diary(Collection<Entry> entries) {
        this.entries.addAll(Objects.requireNonNull(entries));
    }

    public static Diary fromJson(File file) throws StreamReadException,
            DatabindException,
            IOException {
        return new ObjectMapper().readValue(file, Diary.class);
    }

    // query builder
    public static class Query {
        private final List<Entry> entries;
        private Predicate<Entry> restrictions = ((entry) -> true);
        private Query(List<Entry> entries) {
            this.entries = entries;
        }

        public Query after(LocalDateTime date) {
            restrictions = restrictions.and((entry) -> entry.after(date));
            return this;
        }

        public Query before(LocalDateTime date) {
            restrictions = restrictions.and((entry) -> entry.before(date));
            return this;
        }

        public Query contains(String keyword) {
            restrictions = restrictions.and((entry) -> entry.contains(keyword));
            return this;
        }

        public Query contains(List<String> keywordsList) {
            keywordsList
                    .forEach((keyword) -> restrictions = restrictions.and((entry) -> entry.contains(keyword)));
            // TODO, maybe we can't change predicate like this
            return this;
        }

        public Diary select() {
            return new Diary(
                entries.stream()
                        .filter(restrictions)
                        .sorted(Comparator.comparing(Entry::date))
                        .toList() // TODO maybe there is a better way
            );
        }
    }

    public Query query() {
        return new Query(this.entries);
    }

    public boolean remove(String heading) {
        return true; //TODO
    }

    public boolean insert(String heading, String contents) {
        return true; //TODO
    }


    @Override
    public String toString() {
        return "Diary{" +
                "entries=" + entries +
                '}';
    }
}
