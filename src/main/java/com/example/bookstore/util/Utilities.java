package com.example.bookstore.util;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Utilities {

    public static <T> Set<T> singleElementSet(T element)
    {
        Set<T> set = new HashSet<>();
        set.add(element);
        return set;
    }

    public static <K, V> ConcurrentMap<K, V>  singleElementConcurrentMap(K key, V value){
        ConcurrentMap<K, V> map = new ConcurrentHashMap<>();
        map.put(key, value);
        return map;
    }

    public static boolean isLetter(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }



}
