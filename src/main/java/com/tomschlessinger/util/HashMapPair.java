package com.tomschlessinger.util;

import org.joml.Vector2i;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.BiConsumer;

public class HashMapPair<A,B,V> implements Serializable {
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

    public static class Vector2iHashMap<V> {
        public final Map<org.joml.Vector2i, V> map;
        public Vector2iHashMap(){
            map = new HashMap<>();
        }
        public void put(int a, int b, V value){
            map.put(new org.joml.Vector2i(a,b), value);
        }
        public V get(int a, int b){
            return map.get(new org.joml.Vector2i(a,b));
        }
        public V getOrDefault(int a, int b, V defaultValue){
            V v;
            return (((v = get(a,b)) != null) || containsKey(a,b)) ? v : defaultValue;
        }
        public boolean containsKey(int a, int b){
            return map.containsKey(new org.joml.Vector2i(a,b));
        }
        public void forEach(BiConsumer<Vector2i, V> action){
            map.forEach(action);
        }
        public String toString(){
            return map.toString();
        }
        public Collection<V> values(){
            return map.values();
        }
    }
}