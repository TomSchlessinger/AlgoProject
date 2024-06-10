package com.tomschlessinger.world.generate;

import com.tomschlessinger.random.PerlinNoiseGenerator;
import com.tomschlessinger.tile.AbstractTile;
import com.tomschlessinger.tile.TileRegistry;
import com.tomschlessinger.util.Vector2i;
import com.tomschlessinger.world.World;

import java.util.*;

public class TerrainGenerator {
    private final PerlinNoiseGenerator noise;
    private final Map<Integer,Double> surface; // x-pos : surface height
    private final int height;
    private final World world;
    private final Set<Integer> generated;

    public static final int SURFACE_SIZE = 7;//SURFACE HEIGHT to SURFACE HEIGHT + SURFACE_SIZE is top biome
    public static final int SURFACE_HEIGHT = 450;//CAVE_DEPTH < SURFACE_HEIGHT <= world height /// CAVE DEPTH to SURFACE HEIGHT is surface biome
    public static final int CAVE_DEPTH = 300; // DEEP_CAVE_DEPTH < CAVE_DEPTH < SURFACE_HEIGHT /// DEEP CAVE DEPTH to CAVE DEPTH is caves biome
    public static final int DEEP_CAVE_DEPTH = 125; // HELL_DEPTH < DEEP_CAVE_DEPTH < CAVE_DEPTH /// HELL DEPTH to DEEP CAVE DEPTH is deep caves biome
    public static final int HELL_DEPTH = 75; //0<= HELL_DEPTH < DEEP_CAVE_DEPTH /// 0 to HELL_DEPTH is hell biome
    //height/2 to height/2 + SURFACE_HEIGHT: surface
    //CAVES: < height/2 --> SET EVERY FUCKING THING TO STONE!!!!! then cav carver -> fancy schmancy ass fucking maze generator that was put on cocaine and shit and instead of being +x +y is just weird and fucking weird and fuck and shit and

    public TerrainGenerator(World world, int height, long seed) {
        this.noise = new PerlinNoiseGenerator(seed);
        surface = new HashMap<>();
        this.height = height;
        this.world=world;
        generated = new HashSet<>();
    }
    //surface --> y >= 0
    //caves --> y < 0 ---- dirt -> stone then cave carver will do its thing
    //cave carver can be a modified dfs
    public void generateSurface(int x){
        for(int i = CAVE_DEPTH; i < SURFACE_HEIGHT; i++){
            world.setTile(x,i, TileRegistry.getTile("dirt"));
        }
        surface.put(x,5d*noise.noise(x/10f,SURFACE_HEIGHT/10f)+5f);
        //System.out.println("surface: " + surface);
        for(int i = SURFACE_HEIGHT; i < SURFACE_HEIGHT+surface.get(x); i++){
            world.setTile(x,i, TileRegistry.getTile("grass"));
        }
    }
    public void generateCaves(int x){
        for(int i = DEEP_CAVE_DEPTH; i < CAVE_DEPTH; i++){
            world.setTile(x, i, TileRegistry.getTile("stone"));
        }
    }
    public void generateDeepCaves(int x){
        for(int i = HELL_DEPTH; i < DEEP_CAVE_DEPTH; i++){
            world.setTile(x, i, TileRegistry.getTile("hard_stone"));
        }
    }
    public void generateHell(int x){
        for(int i = 0; i < HELL_DEPTH; i++){
            world.setTile(x, i, TileRegistry.getTile("hell"));
        }
    }
    public void generate(int x) {
        generated.add(x);
        generateSurface(x);
        generateCaves(x);
        generateDeepCaves(x);
        generateHell(x);
    }
    public boolean generated(int x){
        return this.generated.contains(x);
    }
    public int getWidth(){return generated.size();}
}
