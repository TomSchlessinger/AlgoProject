package com.tomschlessinger.keybinds;

import com.tomschlessinger.Main;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.*;

public class KeyBinds {
    public static final Map<Integer, Set<Consumer<KeyBindContext>>> keys = new HashMap<>();
    public static final Map<Integer, KeyBindContext> pressed = new HashMap<>();

    public static void registerKey(int key, Consumer<KeyBindContext> task){
        registerKey(key, task, false);
    }
    public static void registerKey(int key, Consumer<KeyBindContext> task, boolean pressed){
        if(pressed) {
            KeyBinds.pressed.put(key,null);
        }
        if(!keys.containsKey(key)) {
            keys.put(key, new HashSet<>());
        }
        keys.get(key).add(task);
    }
    public static void run(int key, KeyBindContext context){
        if(keys.containsKey(key)) {
            if(pressed.containsKey(key)){
                //System.out.println(context);
                if(context.getAction()==GLFW_REPEAT || context.getAction()==GLFW_PRESS) pressed.put(key, context);
                else pressed.put(key,null);
            } else {
                keys.get(key).forEach(c -> c.accept(context));
            }
        }

    }
}
