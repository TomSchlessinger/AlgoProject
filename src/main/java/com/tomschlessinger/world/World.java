package com.tomschlessinger.world;

import com.tomschlessinger.tile.AbstractTile;
import com.tomschlessinger.tile.TileRegistry;
import com.tomschlessinger.util.HashMapPair;
import com.tomschlessinger.util.Vector2i;

import java.util.HashMap;

public class World{
    private final int width;
    private final int height;
    private final HashMapPair<Integer,Integer, AbstractTile> tiles = new HashMapPair<>();
    public World(int width, int height){
        this.width=width;
        this.height=height;
    }

    public Vector2i getDims(){return new Vector2i(width,height);}

    public AbstractTile getTile(int x, int y){
        return !tiles.containsKey(x,y) ? TileRegistry.getTile("air"):tiles.get(x,y);
    }

    public void setTile(int x, int y, AbstractTile t){
        tiles.put(x,y,t);
    }

    public String toString(){
        StringBuilder ret = new StringBuilder();
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                ret.append(getTile(x,y)).append(" | ");
            }
            ret.append("\n");
        }
        return ret.toString();
    }
}
