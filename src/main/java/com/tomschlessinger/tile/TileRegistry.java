package com.tomschlessinger.tile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TileRegistry {
    public static final Map<String,AbstractTile> tiles = new HashMap<>();

    public static void init(){
        register("dirt","dirt.png");
        register("air","air.png", new TileOptions().setSolid(false));//(String)null);
        register("grass","grass.png");
        register("stone","stone.png");
        register("hell","hell2.png");
        register("hard_stone","hard_stone.png");
    }
    public TileRegistry(){
    }

    public static AbstractTile register(String s, String texturePath, TileOptions options){
        return tiles.put(s,new GenericTile(s,texturePath, options));
    }
    public static AbstractTile register(String s, String texturePath){
        return tiles.put(s,new GenericTile(s,texturePath));
    }
    public static AbstractTile register(String s, AbstractTile t){
        return tiles.put(s,t);
    }
    public static AbstractTile getTile(String s){
        return tiles.get(s);
    }
}
