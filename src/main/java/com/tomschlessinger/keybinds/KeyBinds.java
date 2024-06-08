package com.tomschlessinger.keybinds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class KeyBinds {
    public static Map<Integer, Set<Consumer<KeyBindContext>>> keys = new HashMap<>();
    public static void registerKey(int key, Consumer<KeyBindContext> task){
        if(!keys.containsKey(key)) {
            keys.put(key, new HashSet<>());
        }
        keys.get(key).add(task);
    }
    public static void run(int key, KeyBindContext context){
        if(keys.containsKey(key)) {
            keys.get(key).forEach(c -> c.accept(context));
        }
    }
}
