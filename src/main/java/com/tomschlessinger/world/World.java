package com.tomschlessinger.world;

import com.tomschlessinger.Main;
import com.tomschlessinger.collision.BoundingBox;
import com.tomschlessinger.player.Player;
import com.tomschlessinger.render.CameraView;
import com.tomschlessinger.tile.TileRegistry;
import com.tomschlessinger.tile.TileState;
import com.tomschlessinger.util.Vector2i;
import com.tomschlessinger.world.generate.CaveGenerator;
import com.tomschlessinger.world.generate.TerrainGenerator;
import org.joml.Vector2f;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.tomschlessinger.util.HashMapPair.Vector2iHashMap;

public class World{
    //private final int width;
    private final Player player;
    private final TerrainGenerator generator;
    private final CaveGenerator caveGenerator;
    private final int height;
    private final Vector2iHashMap<TileState> tiles = new Vector2iHashMap<>();
    private final CameraView camera;
    private final long seed;
    //private final HashMapPair<Integer, Integer, AbstractTile> tiles = new HashMapPair<>();
    public World(int height, Player player){
        seed = System.currentTimeMillis();
        caveGenerator = new CaveGenerator(this,seed);
//        this.width=width;
        this.camera = new CameraView(this);
        player.setCamera(camera);
        this.player=player;
        this.height=height;
        generator = new TerrainGenerator(this,height,seed);
    }

    public int getHeight(){return height;}

    public TileState getTile(int x, int y){
        TileState ret = tiles.getOrDefault(x,y,
                new TileState(
                        new BoundingBox(new Vector2f(0), new Vector2f(0)),
                        TileRegistry.getTile("air")
                )
        );
        return ret.getTile() != null ? ret : new TileState(
                new BoundingBox(new Vector2f(0), new Vector2f(0)),
                TileRegistry.getTile("air")
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

    public void generate(int initX, int initY){
//        System.out.println("min: " + new Vector2i(initX-2*Main.TEXTURE_SIZE,initY-2*Main.TEXTURE_SIZE).subtract(camera.getOffset()).divide(32));
//        System.out.println("max: " + new Vector2i(initX+Main.SCREEN_WIDTH+2*Main.TEXTURE_SIZE,initY+2*Main.TEXTURE_SIZE+Main.SCREEN_HEIGHT).subtract(camera.getOffset()).divide(32));

        caveGenerator.setLoaded(
                new Vector2i(initX-20*Main.TEXTURE_SIZE,initY-20*Main.TEXTURE_SIZE).subtract(camera.getOffset()).divide(Main.TEXTURE_SIZE),
                new Vector2i(initX+Main.SCREEN_WIDTH+20*Main.TEXTURE_SIZE,initY+20*Main.TEXTURE_SIZE+Main.SCREEN_HEIGHT).subtract(camera.getOffset()).divide(Main.TEXTURE_SIZE)
        );
        //System.out.println("initX: " + initX + " y " + initY);
        //Set<Vector2i> vec = new HashSet<>();
        for(int x = initX-2*Main.TEXTURE_SIZE; x < initX+Main.SCREEN_WIDTH+2*Main.TEXTURE_SIZE; x+=Main.TEXTURE_SIZE) {
//            for(int y = initY-2*Main.TEXTURE_SIZE; y < initY+2*Main.TEXTURE_SIZE+Main.SCREEN_HEIGHT; y+=Main.TEXTURE_SIZE){
//                vec.add(new Vector2i(x,y).subtract(camera.getOffset().getX(),camera.getOffset().getY()).divide(Main.TEXTURE_SIZE));
//            }
            if(!generator.generated(x/Main.TEXTURE_SIZE)){
                //System.out.println("generated blocks at x pos " + x/Main.TEXTURE_SIZE);
                generator.generate(x/Main.TEXTURE_SIZE);

            }
        }
        //System.out.println(vec);
        AtomicReference<Double> minDist = new AtomicReference<>(Double.MAX_VALUE);
        Set<Vector2i> combined = new HashSet<>(caveGenerator.getGenerated());
        combined.addAll(caveGenerator.getQueue());
        combined.forEach(
                (vec) -> minDist.set(Math.min(minDist.get(), vec.distance(initX/32, initY/32)))
        );
        //System.out.println(minDist.get());
        if(minDist.get() > Main.CAVE_DISTANCE_THRESHOLD){
            Random random = new Random(seed);
//            System.out.println("generating new cave");
            int signX = initX < 0 ? -1:1;
            caveGenerator.generateCave(initX/32 + signX * random.nextInt(50),initY/32 - random.nextInt(50));
        }
        caveGenerator.generateCave();
    }

    public static boolean isLoaded(Vector2i pos, Vector2i min, Vector2i max){
//        max.subtract(pos);
//        min.subtract(pos);
        return pos.getX() >= min.getX() && pos.getX() <= max.getX() && pos.getY() >= min.getY() && pos.getY() <= max.getY();
    }

    public void tick(){
        //System.out.println("pos: " + player.getPos().copy().divide(32));
        player.tick(camera);
        //player.collideWithTiles(this);
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
    public CaveGenerator getCaveGenerator(){
        return caveGenerator;
    }
}
