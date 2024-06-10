package com.tomschlessinger.render;

import com.tomschlessinger.Main;
import com.tomschlessinger.tile.AbstractTile;
import com.tomschlessinger.util.Vector2i;
import com.tomschlessinger.world.World;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;

public class CameraView {
    private final Map<AbstractTile, Set<Vector2i>> renderingQueue;
    private final Vector2i pos;
    private final Vector2i offset;
    private float zoom = 1;
    private final World world;
    public CameraView(World world) {
        this.world = world;
        offset = new Vector2i(Main.SCREEN_WIDTH/2,Main.SCREEN_HEIGHT/2);
        pos = Vector2i.ZERO.copy().subtract(0,0);
        renderingQueue = new HashMap<>();
    }
    public void move(int dx, int dy) {
        pos.add(dx,dy);
//        System.out.println("camera pos: (" + (pos.getX()+offset.getX()) + ", " + (pos.getY()+offset.getY()) + ")");
//        System.out.println("actual pos: (" + (200-pos.getX())+ ", " + (200-pos.getY()) + ")");
    }

    public void zoom(float delta) {
        this.zoom += delta;
    }

    public void renderWorld(int width, int height) {
        renderingQueue.clear();
//        System.out.println("DRAWING SHIT FROM " + pos.copy() + " to " + pos.copy().add(width,height));
//        for(int x = -1; x < width/Main.TEXTURE_SIZE+1; x++){
//            for(int y = -1; y < height/Main.TEXTURE_SIZE+1; y++){
//                AbstractTile tile = world.getTile(pos.copy().add(x,y));
//                if(!renderingQueue.containsKey(tile)){
//                    renderingQueue.put(tile, new HashSet<>());
//                }
//                renderingQueue.get(tile).add(new Vector2i(x*Main.TEXTURE_SIZE,y*Main.TEXTURE_SIZE+pos.getY()%32));
//            }
//        }
        for(int x = -Main.TEXTURE_SIZE; x < width+Main.TEXTURE_SIZE; x+=Main.TEXTURE_SIZE) {
            for(int y = -Main.TEXTURE_SIZE; y < height+Main.TEXTURE_SIZE; y+=Main.TEXTURE_SIZE){ //x,y are pos on the screen; pos + x,y --> world position; (pos+x,y)/32 --> block pos
                AbstractTile tile = world.getTile(pos.copy().add(x,y).divide(Main.TEXTURE_SIZE));
                if(!renderingQueue.containsKey(tile)){
                    renderingQueue.put(tile, new HashSet<>());
                }
                renderingQueue.get(tile).add(new Vector2i(x,y).subtract(pos.copy().mod(Main.TEXTURE_SIZE)));
            }
        }
//        System.out.println("TILE AT " + pos.copy().divide(Main.TEXTURE_SIZE) + " is " + world.getTile(pos.copy().divide(Main.TEXTURE_SIZE)) );
//        System.out.println("TILE AT " + pos.copy().add(width,height).divide(Main.TEXTURE_SIZE) + " is " + world.getTile(pos.copy().add(width,height).divide(Main.TEXTURE_SIZE)));
        renderingQueue.forEach(
                (tile, posList) -> {
                    tile.bind();
                    glBegin(GL_QUADS);
                    posList.forEach(
                            p -> {
                                int x = p.getX();
                                int y = height - 1 - p.getY();
                                glTexCoord2f(0, 0);
                                glVertex2f(x, y);

                                glTexCoord2f(1, 0);
                                glVertex2f(x+Main.TEXTURE_SIZE, y);

                                glTexCoord2f(1, 1);
                                glVertex2f(x+Main.TEXTURE_SIZE, y+Main.TEXTURE_SIZE);

                                glTexCoord2f(0, 1);
                                glVertex2f(x, y+Main.TEXTURE_SIZE);
                            }
                    );
                    glEnd();
                }
        );
    }

    public Vector2i getWorldPos(){
        return pos.copy();
    }
}