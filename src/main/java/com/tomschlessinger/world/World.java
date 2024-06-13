package com.tomschlessinger.world;

import com.tomschlessinger.Main;
import com.tomschlessinger.collision.BoundingBox;
import com.tomschlessinger.player.Player;
import com.tomschlessinger.render.CameraView;
import com.tomschlessinger.tile.AbstractTile;
import com.tomschlessinger.tile.TileRegistry;
import com.tomschlessinger.tile.TileState;
import com.tomschlessinger.util.Vector2i;
import com.tomschlessinger.world.generate.TerrainGenerator;
import org.joml.Vector2f;

import java.util.HashMap;

import static com.tomschlessinger.util.HashMapPair.Vector2iHashMap;

public class World{
    //private final int width;
    private final Player player;
    private final TerrainGenerator generator;
    private final int height;
    private final Vector2iHashMap<TileState> tiles = new Vector2iHashMap<>();
    private final CameraView camera;
    //private final HashMapPair<Integer, Integer, AbstractTile> tiles = new HashMapPair<>();
    public World(int height, Player player){
//        this.width=width;
        this.camera = new CameraView(this);
        player.setCamera(camera);
        this.player=player;
        this.height=height;
        generator = new TerrainGenerator(this,height,System.currentTimeMillis());
    }

    public int getHeight(){return height;}

    public TileState getTile(int x, int y){
        return tiles.getOrDefault(x,y,
                new TileState(
                        new BoundingBox(new Vector2f(0), new Vector2f(0)),
                        TileRegistry.getTile("air")
                )
        );
    }
    public TileState getTile(Vector2i pos){
        return getTile(pos.getX(),pos.getY());
    }

    public boolean isSolid(Vector2i pos){
        return getTile(pos).getTile().isSolid();
    }
    public boolean isSolid(int x, int y){
        return getTile(x,y).getTile().isSolid();
    }

    public void setTile(int x, int y, TileState t){
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
        player.tick(camera);
        player.collideWithTiles(this);
    }
    public BoundingBox getTileBoundingBox(int x, int y) {
        try {
            return getTile(x,y).getBoundingBox();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    public CameraView getCamera(){
        return camera;
    }

    public void render() {
        camera.renderWorld(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        player.render();
        //player.render();
    }
}
