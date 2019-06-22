package com.github.cdarts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class IntegerFSTBuilder extends FSTBuilder<Integer> {
    @Override
    Integer defaultValue() {
        return 0;
    }

    @Override
    Optional<Integer> prefix(Optional<Integer> a, Optional<Integer> b) {
        if (a.isEmpty() || b.isEmpty()) {
            return Optional.empty();
        }
        return a.get() == b.get() ? a : Optional.empty();
    }

    @Override
    Optional<Integer> concat(Optional<Integer> a, Optional<Integer> b) {
        if (a.isEmpty()) {
            return b;
        }
        if (b.isEmpty()) {
            return a;
        }
        if (a.equals(b)) {
            return a;
        }
        // adopt former value
        return a;
    }

    @Override
    Optional<Integer> subtract(Optional<Integer> a, Optional<Integer> b) {
        if (a.isEmpty()) {
            return Optional.empty();
        }
        if (b.isEmpty()) {
            return a;
        }
        if (a.equals(b)) {
            return Optional.empty();
        }
        // ignore subtrahend
        return a;
    }

    static int compare(byte[] b1, byte[] b2) {
        final int shorterLength = Math.min(b1.length, b2.length);
        for (int i = 0; i < shorterLength; i++) {
            if (b1[i] != b2[i]) {
                int i1 = ((int) b1[i]) & 0xFF;
                int i2 = ((int) b2[i]) & 0xFF;
                return i1 - i2;
            }
        }
        return b1.length - b2.length;
    }

    public static void main(String[] args) throws Exception {
        final List<Map.Entry<String, Integer>> lexicon = new ArrayList<>();
        lexicon.add(Map.entry("mop", 0));
        lexicon.add(Map.entry("moth", 1));
        lexicon.add(Map.entry("pop", 2));
        lexicon.add(Map.entry("star", 3));
        lexicon.add(Map.entry("stop", 4));
        lexicon.add(Map.entry("top", 5));

        final var entries = lexicon.stream().map(entry -> Map
                .entry(entry.getKey().getBytes(java.nio.charset.StandardCharsets.US_ASCII), entry.getValue()));

        final var builder = new IntegerFSTBuilder();
        final var fst = builder.build(entries);
        System.out.println(fst.translateToDot());
    }
}