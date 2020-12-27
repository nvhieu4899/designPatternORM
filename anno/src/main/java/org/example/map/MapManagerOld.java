package org.example.map;

import org.apache.commons.collections4.BidiMap;

import java.util.HashMap;

public class MapManagerOld {
    private static HashMap<String, BidiMap<String, String>> maps = new HashMap<>();

    public static BidiMap<String, String> getMap(String className) {
        if (maps.containsKey(className))
            return maps.get(className);

        return null;
    }

    public static void addMap(String key, BidiMap<String, String> map) {
        System.out.println(maps.size());
        maps.put(key, map);
    }

}
