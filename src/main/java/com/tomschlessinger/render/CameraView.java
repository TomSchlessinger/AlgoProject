package com.tomschlessinger.render;

import com.tomschlessinger.Main;
import com.tomschlessinger.tile.AbstractTile;
import com.tomschlessinger.tile.TileState;
import com.tomschlessinger.util.Vector2f;
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
    private final Map<TileState, Set<Vector2i>> renderingQueue;
    private final Vector2i pos;
    private final Vector2i offset;
    private float zoom = 1;
    private final World world;
    public CameraView(World world) {
        this.world = world;
        offset = Vector2i.ZERO.copy().add(20,32*450);
        pos = Vector2i.ZERO.copy();
        renderingQueue = new HashMap<>();
    }
    public void move(Vector2i vec){
        pos.add(vec);
    }
    public void move(int dx, int dy) {
        pos.add(dx,dy);
    }

    public void zoom(float delta) {
        this.zoom += delta;
    }

    public void renderWorld(int width, int height) {
        renderingQueue.clear();
        for(int x = -Main.TEXTURE_SIZE; x < width+Main.TEXTURE_SIZE; x+=Main.TEXTURE_SIZE) {
            for(int y = -Main.TEXTURE_SIZE; y < height+Main.TEXTURE_SIZE; y+=Main.TEXTURE_SIZE){ //x,y are pos on the screen; pos + x,y --> world position; (pos+x,y)/32 --> block pos
                TileState tile = world.getTile(getPos().add(x,y).divide(Main.TEXTURE_SIZE));
                //System.out.println("gonna render: " + tile.getTile());
                if(!renderingQueue.containsKey(tile)){
                    renderingQueue.put(tile, new HashSet<>());
                }
//pos.copy().mod(Main.TEXTURE_SIZE)
                renderingQueue.get(tile).add(new Vector2i(x,y).subtract((pos.getX())%32,(1+pos.getY())%32));
            }
        }
//        System.out.println("TILE AT " + pos.copy().divide(Main.TEXTURE_SIZE) + " is " + world.getTile(pos.copy().divide(Main.TEXTURE_SIZE)) );
//        System.out.println("TILE AT " + pos.copy().add(width,height).divide(Main.TEXTURE_SIZE) + " is " + world.getTile(pos.copy().add(width,height).divide(Main.TEXTURE_SIZE)));
        renderingQueue.forEach(
                (tile, posList) -> {
                    //System.out.println("tryna render: " + tile);
                    tile.getTile().bind();
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
        return getPos();
    }
    public Vector2i getPos() {
        return pos.copy().add(offset);
    }
    public void setPos(Vector2i vec){
        this.pos.set(vec);
    }
    public Vector2i getOffset(){
        return offset;
    }
    public String toString(){return getPos().toString();}
}