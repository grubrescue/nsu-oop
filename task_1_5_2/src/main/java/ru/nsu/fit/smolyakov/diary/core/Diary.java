package ru.nsu.fit.smolyakov.diary.core;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Diary {
    private final SortedSet<Entry> entries
            = new TreeSet<Entry>(Comparator.comparing(Entry::date)); // TODO think about container here

    // TODO [de]serialization
    public Diary(SortedSet<Entry> entries) {
        this.entries.addAll(Objects.requireNonNull(entries));
    }

    private Diary(Stream<Entry> entryStream) {
        this.entries.addAll(entryStream.toList()); // TODO once again, maybe there is a better way
    }

    // query builder
    public static class Query {
        private final SortedSet<Entry> entries;
        private Predicate<Entry> restrictions = ((entry) -> true);
        private Query(SortedSet<Entry> entries) {
            this.entries = entries;
        }

        public Query til(LocalDateTime date) {
            restrictions = restrictions.and((x) -> x.til(date));
            return this;
        }

        public Query until(LocalDateTime date) {
            restrictions = restrictions.and((x) -> x.until(date));
            return this;
        }

        public Query contains(String keyword) {
            restrictions = restrictions.and((x) -> x.contains(keyword));
            return this;
        }

        public Diary select() {
            return new Diary(
                entries.stream()
                        .filter(restrictions) // TODO maybe there is a better way
            );
        }
    }

    public Query query() {
        return new Query(this.entries);
    }
}
