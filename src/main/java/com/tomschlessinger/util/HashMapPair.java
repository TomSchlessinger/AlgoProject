package com.tomschlessinger.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class HashMapPair<A,B,V> implements Cloneable, Serializable {
    private final Map<Pair<A,B>,V> map;
    public HashMapPair(){
        map = new HashMap<>();
    }
    public void put(A a, B b, V value){
        map.put(Pair.of(a,b),value);
    }
    public V get(A a, B b){
        return map.get(Pair.of(a,b));
    }
    public boolean containsKey(A a, B b){
        return map.containsKey(Pair.of(a,b));
    }

    public static class Vector2iHashMap<V> implements Cloneable, Serializable {
        public final Map<Vector2i, V> map;
        public Vector2iHashMap(){
            map = new HashMap<>();
        }
        public void put(int a, int b, V value){
            map.put(new Vector2i(a,b), value);
        }
        public V get(int a, int b){
            return map.get(new Vector2i(a,b));
        }
        public boolean containsKey(int a, int b){
            return map.containsKey(new Vector2i(a,b));
        }
    }
}