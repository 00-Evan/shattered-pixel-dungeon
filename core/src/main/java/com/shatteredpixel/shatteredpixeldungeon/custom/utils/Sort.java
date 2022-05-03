package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Sort {
    public static <K, V> LinkedHashMap<K, V> sortHashMap(HashMap<K, V> map, Comparator<Map.Entry<K, V>> comparator){
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, comparator);
        LinkedHashMap<K, V> LHM = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            LHM.put(entry.getKey(), entry.getValue());
        }
        return LHM;
    }
}
