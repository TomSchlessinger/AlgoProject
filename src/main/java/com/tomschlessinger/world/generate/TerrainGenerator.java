package com.tomschlessinger.world.generate;

import com.tomschlessinger.random.PerlinNoiseGenerator;
import com.tomschlessinger.tile.AbstractTile;
import com.tomschlessinger.tile.TileRegistry;
import com.tomschlessinger.util.HashMapPair;
import com.tomschlessinger.world.World;

import java.util.Arrays;

public class TerrainGenerator {
    private final int width;
    private final int height;
    private final PerlinNoiseGenerator noise;
    private float[] surface;

    public TerrainGenerator(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.noise = new PerlinNoiseGenerator(seed);
        surface = new float[width];
    }
    //surface --> y >= 0
    //caves --> y < 0 ---- dirt -> stone then cave carver will do its thing
    //cave carver can be a modified dfs
    public void generateSurface(World world) {
        float[][] temp = new float[width][height];
        float[] heightMap = new float[width];
        float scale = 0.1f;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                temp[x][y] = (float) (noise.noise(x * scale, y * scale) * 0.5 + 0.5);
            }
        }
        for (int x = 0; x < width; x++) {
//            float sum = 0;
//            for (int y = 0; y < height; y++) {
//                sum+=temp[x][y];
//            }
            heightMap[x] = temp[x][0]*10;
        }
        System.out.println("height: " + Arrays.toString(heightMap));
        this.surface = heightMap;
        for(int i = 0; i < width; i++){
            for(int j = 0; j < (int)heightMap[i]; j++){
                //System.out.println("height: "+ j);
                AbstractTile t = TileRegistry.getTile("dirt");
                //System.out.println("block : " + t);
                world.setTile(i,height-j, t);
            }
        }
    }


    public String toString(){
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < surface.length; i++){
            s.append("|");
        }
        return s.toString();
    }
}
