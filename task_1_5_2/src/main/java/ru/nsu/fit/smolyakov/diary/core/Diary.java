package ru.nsu.fit.smolyakov.diary.core;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Diary {
    private final SortedSet<Entry> entries
            = new TreeSet<Entry>(Comparator.comparing(Entry::date)); // TODO maybe arraylist will suit better?

    // TODO [de]serialization
    public Diary(Collection<Entry> entries) {
        this.entries.addAll(Objects.requireNonNull(entries));
    }

    // query builder
    public static class Query {
        private final SortedSet<Entry> entries;
        private Predicate<Entry> restrictions = ((entry) -> true);
        private Query(SortedSet<Entry> entries) {
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

        public Diary select() {
            return new Diary(
                entries.stream()
                        .filter(restrictions)
                        .toList() // TODO maybe there is a better way
            );
        }
    }

    public Query query() {
        return new Query(this.entries);
    }


    @Override
    public String toString() {
        return "Diary{" +
                "entries=" + entries +
                '}';
    }

}
