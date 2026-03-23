package com.sportsvista.model;

import java.util.HashMap;
import java.util.Map;

public class SportRegistry {
    private static final Map<String, Integer> sportKeyToId = new HashMap<>();

    public static void register(String key, int id) {
        sportKeyToId.put(key, id);
    }

    public static Integer getId(String key) {
        return sportKeyToId.get(key);
    }
}
