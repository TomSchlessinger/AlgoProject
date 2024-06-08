package com.tomschlessinger.tile;

import org.lwjgl.openvr.Texture;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TileRegistry {
    public static final Map<String,AbstractTile> tiles = new HashMap<>();
    static{
        register("air","");
        register("dirt","dirt.png");
        register("grass","grass.png");
    }
    public static void register(String s, String texturePath){
        tiles.put(s,new GenericTile(s,texturePath));
    }
    public static void register(String s, AbstractTile t){
        tiles.put(s,t);
    }
    public static AbstractTile getTile(String s){
        return tiles.getOrDefault(s, null);
    }
}
