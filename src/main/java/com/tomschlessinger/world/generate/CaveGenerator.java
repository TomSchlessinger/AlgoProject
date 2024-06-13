package com.tomschlessinger.world.generate;

import com.tomschlessinger.collision.BoundingBox;
import com.tomschlessinger.random.PerlinNoiseGenerator;
import com.tomschlessinger.tile.TileRegistry;
import com.tomschlessinger.tile.TileState;
import com.tomschlessinger.util.HashMapPair;
import com.tomschlessinger.world.World;
import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CaveGenerator {
    private final World world;
    private final long seed;
    private final Random random;
    private final PerlinNoiseGenerator noise;
    private final HashMapPair.Vector2iHashMap<Integer> coords = new HashMapPair.Vector2iHashMap<>();
    private final Set<Vector2i> loaded = new HashSet<>();
    private final Set<Vector2i> generated = new HashSet<>();
    private final Set<Vector2i> genQueue = new HashSet<>();
    private final Set<com.tomschlessinger.util.Vector2i> caves = new HashSet<>();
    public CaveGenerator(World world, long seed){
        this.world=world;
        this.seed=seed;
        random=new Random(seed);
        noise = new PerlinNoiseGenerator(seed);
    }
    public void generateCircle(int x, int y, int r){
        //System.out.println("drawing circle with stats x = " + x + " y = " + y + " r = " + r);
        /*
        idea: create a (2r+1)*(2r+1) box, centered around (x,y) --> for i in range(x-r,x+r) ; for j in range(y-r,y+r)
        go tile-by-tile in that circle and replace any blocks that are <= r distance from x,y with air
        */
        for(int cX = x-r; cX < x+r; cX++){
            for(int cY = y-r; cY < y+r; cY++){
                if(new Vector2i(x,y).distance(cX,cY)<=r){
                    caves.add(new com.tomschlessinger.util.Vector2i(cX,cY));
                    world.setTile(cX,cY, new TileState(
                                    new BoundingBox(new Vector2f(0), new Vector2f(0)),
                                    TileRegistry.getTile("air")
                            ));
                }
            }
        }
    }
    public void generateCave(int x, int y, int r){
        generateCircle(x,y,r);
        generateCave(x,y);
    }

    public float sillyNoise(double d){
        int maxmin = 50;
        Random r = new Random((long)d);
        float j = r.nextFloat()%(2*maxmin+1)-maxmin;
        return j;
    }
    public void generateCave(int x, int y){
        if(generated.contains(new Vector2i(x,y)))return;
        if(!loaded.contains(new Vector2i(x,y))){
            genQueue.add(new Vector2i(x,y));
            return;
        }
        int size =(int)(50d*noise.noise(sillyNoise(x*x+x+x+y),sillyNoise(y*y+y+y+x)));//I HAVE NO IDEA WHAT IM DOING
        double index1 = 100*noise.noise(sillyNoise(x*x*x),sillyNoise(x+x+x+y));
        double index2 = 100*noise.noise(sillyNoise(y*y*y),sillyNoise(y+y+y+x));
//        System.out.println("old indexes: " + index1 +" " + index2);
//        System.out.println("generated cave at (" + x + ", "+ y +") with radius " + size);
        size = (int)constrain(size,0,25);
        int ind1 = (int)constrain((long)map(index1,-60,60,0,2 * size - 1),0,(long)size*2-1)-size;
        int ind2 = (int)constrain((long)map(index2,-60,60,0,2 * size - 1),0,(long)size*2-1)-size;
//        System.out.println("new indexes: " + ind1 +" " + ind2);
//        System.out.println("generated cave at (" + x + ", "+ y +") with radius " + size);
        coords.put(x,y,size);
        generated.add(new Vector2i(x,y));
        if(!loaded.contains(new Vector2i(ind1,ind2))){
            genQueue.add(new Vector2i(ind1,ind2));
        }else{
            generateCave(ind1, ind2);
        }
        generateCave();
    }

    public void generateCave(){
        //System.out.println("generate cave called with loaded having " + loaded.size() + " elements\ngenerated has " + generated.size() + " elements\ngenQueue has " + genQueue.size() + " elements ");
        //System.out.println("queue: " + genQueue);
        Set<Vector2i> remQueue = new HashSet<>();
        genQueue.forEach(
                vec -> {
                    //System.out.println(loaded);
                    if(loaded.contains(vec)) {
                        generateCave(vec.x,vec.y,coords.get(vec.x,vec.y));
                        remQueue.add(vec);
                        generated.add(vec);
                    }
                }
        );
        remQueue.forEach(genQueue::remove);
    }

    double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
    long constrain(long x, long x_min, long x_max){
        return ((x)<(x_min)?(x_min):(Math.min((x), (x_max))));
    }

    public void setLoaded(Set<Vector2i> loaded){
        this.loaded.clear();
        this.loaded.addAll(loaded);
    }

    public Set<com.tomschlessinger.util.Vector2i> getGenerated(){
        return caves;
    }
}
