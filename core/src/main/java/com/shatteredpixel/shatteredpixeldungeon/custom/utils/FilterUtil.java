package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FilterUtil {
    public static <T> List<T> filter(Collection<T> data, Filter<T> condition) {
        List<T> result = new ArrayList<>();
        if (condition != null) {
            for (T t : data) {
                if (condition.pass(t)) {
                    result.add(t);
                }
            }
        } else {
            return new ArrayList<>(data);
        }
        return result;
    }

    public interface Filter<T> {
        boolean pass(T t);
    }
}
