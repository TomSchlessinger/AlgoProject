package com.tomschlessinger.tile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TileRegistry {
    public static final Map<String,AbstractTile> tiles = new HashMap<>();
    static  {
        register("dirt","dirt.png");
        register("air","air.png");//(String)null);
        register("grass","grass.png");
        register("stone","stone.png");
        register("hell","hell2.png");
        register("hard_stone","hard_stone.png");
    }

    public static void init(){
    }
    public TileRegistry(){
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
