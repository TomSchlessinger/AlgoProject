package com.tomschlessinger.world;

import com.tomschlessinger.Main;
import com.tomschlessinger.player.Player;
import com.tomschlessinger.tile.AbstractTile;
import com.tomschlessinger.tile.TileRegistry;
import com.tomschlessinger.util.Vector2i;
import com.tomschlessinger.world.generate.TerrainGenerator;

import java.util.HashMap;

import static com.tomschlessinger.util.HashMapPair.Vector2iHashMap;

public class World{
    //private final int width;
    private final TerrainGenerator generator;
    private final int height;
    private final Vector2iHashMap<AbstractTile> tiles = new Vector2iHashMap<>();
    //private final HashMapPair<Integer, Integer, AbstractTile> tiles = new HashMapPair<>();
    public World(int height){
//        this.width=width;
        this.height=height;
        generator = new TerrainGenerator(this,height,System.currentTimeMillis());
    }

    public int getHeight(){return height;}

    public AbstractTile getTile(int x, int y){
        //System.out.println("contanins " + new Vector2i(x,y2) + "?" + tiles.containsKey(x,y2) + " tile: " + tiles.get(x,y2));
        return !tiles.containsKey(x,y) ? TileRegistry.getTile("air") : tiles.get(x,y);
        //0 <= x < width;
        //0 <= y < height
    }
    public AbstractTile getTile(Vector2i pos){
        return getTile(pos.getX(),pos.getY());
    }

    public void setTile(int x, int y, AbstractTile t){
        tiles.put(x,y,t);
    }

    public String toString(){
        StringBuilder ret = new StringBuilder();
        for(int y = 0; y < height; y++){
            for(int x = 0; x < generator.getWidth(); x++){
                ret.append(getTile(x,height-y-1)).append(" | ");
            }
            ret.append("\n");
        }
        return ret.toString();
    }

    public void generate(int init){
        for(int x = init-2*Main.TEXTURE_SIZE; x < init+Main.SCREEN_WIDTH+2*Main.TEXTURE_SIZE; x+=Main.TEXTURE_SIZE) {
            if(!generator.generated(x/Main.TEXTURE_SIZE)){
                //System.out.println("generated blocks at x pos " + x/Main.TEXTURE_SIZE);
                generator.generate(x/ Main.TEXTURE_SIZE);
            }


        }
    }

    public void tick(){
    }
}
