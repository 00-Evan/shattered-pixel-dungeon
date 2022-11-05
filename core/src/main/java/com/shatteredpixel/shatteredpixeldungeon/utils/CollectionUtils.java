package com.shatteredpixel.shatteredpixeldungeon.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class CollectionUtils {

    public static <T> List<T> filter(Iterable<T> collection, Predicate<T> condition) {
        List<T> result = new ArrayList<>();
        for (T element : collection) {
            if (condition.test(element)) {
                result.add(element);
            }
        }

        return result;
    }

    public static <T> T getFirst(Iterable<T> collection, Predicate<T> condition) {
        for (T element : collection) {
            if (condition.test(element)) {
                return element;
            }
        }

        return null;
    }

    public static <T> boolean allMatch(Iterable<T> collection, Predicate<T> condition) {
        for (T element : collection) {
            if (!condition.test(element)) {
                return false;
            }
        }

        return true;
    }

    public static <T> boolean anyMatch(Iterable<T> collection, Predicate<T> condition) {
        for (T element : collection) {
            if (condition.test(element)) {
                return true;
            }
        }

        return false;
    }

    public interface MapBuilder<K, V> {

        MapBuilder<K, V> put(K key, V value);

        Map<K, V> build();
    }

    static <K, V> MapBuilder<K, V> mapBuilder(final Map<K, V> map) {
        return new MapBuilder<K, V>() {
            @Override
            public MapBuilder<K, V> put(K key, V value) {
                map.put(key, value);
                return this;
            }

            @Override
            public Map<K, V> build() {
                return Collections.unmodifiableMap(map);
            }
        };
    }

    public static <K, V> MapBuilder<K, V> mapBuilder() {
        return mapBuilder(new HashMap<>());
    }

    public static <K, V> MapBuilder<K, V> linkedMapBuilder() {
        return mapBuilder(new LinkedHashMap<>());
    }
}
