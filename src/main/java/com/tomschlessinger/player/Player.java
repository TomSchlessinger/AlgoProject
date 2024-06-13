package com.tomschlessinger.player;

import com.tomschlessinger.Main;
import com.tomschlessinger.collision.BoundingBox;
import com.tomschlessinger.collision.Collision;
import com.tomschlessinger.render.CameraView;
import com.tomschlessinger.texture.Texture;
import com.tomschlessinger.tile.TileRegistry;
import com.tomschlessinger.tile.TileState;
import com.tomschlessinger.util.HashMapPair;
import com.tomschlessinger.util.Vector2i;
import com.tomschlessinger.world.World;
import com.tomschlessinger.world.generate.TerrainGenerator;
import org.joml.Vector2f;
import org.lwjgl.util.freetype.FreeType;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_QUADS;

public class Player {
    private final Vector2i pos;
    private final Vector2i vel;
    private final Vector2i acc;
    private final Texture texture;
    private final BoundingBox collisionBox;
    private CameraView camera;
    private final Vector2i tempVel = Vector2i.ZERO.copy();

    public Player(String textureFile){
        texture = new Texture("/assets/textures/player/" + textureFile);
        pos = Vector2i.ZERO.copy();
        vel = Vector2i.ZERO.copy();
        acc = Vector2i.ZERO.copy();
        collisionBox = new BoundingBox(new Vector2f(0), new Vector2f(16f));
    }

    public void setCamera(CameraView camera){
        this.camera=camera;
    }
    public CameraView getCamera(){
        return camera;
    }

    public void tick(CameraView camera){
//        System.out.println("player pos: " + pos);
//        System.out.println("camera pos: " + camera);
        //handleCollisions();
        pos.add(vel);
        pos.add(tempVel);
        camera.setPos(pos.copy());
        collisionBox.moveCenter(getPos().getX()/32f,getPos().getY()/32f);
        int playerY = camera.getOffset().getY()/32+pos.getY()/32+Main.SCREEN_HEIGHT/64;
        int playerX = pos.getX()/32;
//        System.out.println("current tile: " + world.getTile(playerX,playerY).getTile());
//        System.out.println("pos checked: " + new Vector2i(playerX,playerY));
//        System.out.println("current tile: " + world.getTile(pos.getX()/32, (pos.getY() + camera.getOffset().getY())/32).getTile());
//        System.out.println("checking pos: " + new Vector2i(pos.getX()/32, (pos.getY() + camera.getOffset().getY())/32));
        //32 pixels per tile in each axis --> divide by 32 to get tile pos (position in the tile hashmap of a tile) from real pos
//        System.out.println("camera: " + camera.getPos());
//        System.out.println("pos " + pos);
//        System.out.println("collision box center: " + collisionBox.getCenter());
        //vel.add(acc);
        tempVel.set(Vector2i.ZERO);
    }
    public void setVel(Vector2i vel){
        this.vel.set(vel);
    }
    public void setVel(int x, int y){
        vel.setX(x);
        vel.setY(y);
    }
    public void addVel(Vector2i vel){
        this.vel.set(vel);
    }
    public void addVel(int x, int y){
        vel.setX(x);
        vel.setY(y);
    }
    public void move(Vector2i vel){//more like delta v but who caresssss
        tempVel.add(vel);
    }
    public void move(int dx, int dy){
        tempVel.add(dx,dy);
    }
    public void resetVel(){vel.set(Vector2i.ZERO);}
    public void accelerate(int vX, int vY){
        this.acc.add(vX,vY);
    }
//    public void handleCollisions(){
//        Vector2i pos = camera.getRealPos().divide(32);
//        int vX = vel.getX();
//        int vY = vel.getY();
//        int newX = pos.getX()+vX;
//        int newY = pos.getY()+vY;
////        System.out.println("im  currently at " + pos + "which is " + world.getTile(newX,newY));
////        System.out.println("i will be at " + new Vector2i(newX,newY) + " which is " + world.getTile(newX,newY));
//        //System.out.println("next coords" + new Vector2i(newX, newY));
//        System.out.println(newX + ", " + newY + " is air: " + world.isAir(newX,newY));
//        if(world.isSolid(newX,newY) || world.isSolid(pos)){
////            vel.set(Vector2i.ZERO);
////            acc.set(Vector2i.ZERO);
//            System.out.println("colliding");
//            boolean foundX = true;
//            boolean foundY = true;
//            System.out.println("old vel: " + vel);
//            for(int x = 0; x < vX; x++){
//                for(int y = 0; y < vY; y++){
//                    newX = pos.getX()+x;
//                    newY = pos.getY()+y;
//                    if(foundX && !world.isAir(newX,newY)) {
//                        foundX = false;
//                        vel.setX(0);
//                        acc.setX(0);
//                    }
//                    if(foundY && !world.isAir(newX,newY)) {
//                        foundY = false;
//                        vel.setY(0);
//                        acc.setY(0);
//                    }
//                }
//            }
//
//            System.out.println("new vel: " + vel);
//
//            if(foundX) {
//                vel.setX(0);
//                acc.setX(0);
//            }
//            if(foundY) {
//                vel.setY(0);
//                acc.setY(0);
//            }
//        }
//    }
    public double distanceTo(Vector2i v){
        return pos.copy().subtract(v).normalize();
    }

    public void render(){
        texture.bind();
        glBegin(GL_QUADS);

        float xMax = Main.SCREEN_WIDTH;
        float yMax = Main.SCREEN_HEIGHT;
        float x = texture.getWidth();
        float y = texture.getHeight();

        glTexCoord2f(0, 0);
        glVertex2f(xMax/2-x/2, yMax/2-y/2);

        glTexCoord2f(1, 0);
        glVertex2f(xMax/2+x/2, yMax/2-y/2);

        glTexCoord2f(1, 1);
        glVertex2f(xMax/2+x/2, yMax/2+y/2);

        glTexCoord2f(0, 1);
        glVertex2f(xMax/2-x/2, yMax/2+y/2);
        glEnd();
    }
//
    public void collideWithTiles(World world) {
        int n = 32;
        int px = pos.getX();
        int py = camera.getOffset().getY()+pos.getY()+Main.SCREEN_HEIGHT/2;
        System.out.println("before: " + pos);
        TileState[] states = new TileState[n*n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                TileState state = world.getTile(new Vector2i((int) (((px)) - (n / 2)) + i, (int) ((py) + (n / 2)) - j).divide(32));
                if(!state.getTile().isSolid())state=null;
                //if(state!=null) System.out.println("state at " + new Vector2i((int) (((px)) - (n / 2)) + i, (int) (((py)) + (n / 2)) - j).divide(32) + " is " + state.getTile());
                states[i + j * n] = state;
            }
        }

        TileState state = null;
        for (TileState value : states) {
            if (value != null) {
                if (state==null) state = value;
                //System.out.println("collision at" + state.getBoundingBox().getCenter());
                Vector2f length1 = state.getBoundingBox().getCenter().sub(px, py, new Vector2f());
                Vector2f length2 = value.getBoundingBox().getCenter().sub(px, py, new Vector2f());

                if (length1.lengthSquared() > length2.lengthSquared()) {
                    state = value;
                }
            }
        }
        if (state != null) {
            Collision data = collisionBox.getCollision(state.getBoundingBox());
            if (data.isIntersecting) {
                collisionBox.correctPosition(state.getBoundingBox(), data);
                pos.set(boxToPlayer(collisionBox.getCenter(),world));
            }

            for (TileState state1 : states) {
                if (state1 != null) {

                    Vector2f length1 = state.getBoundingBox().getCenter().sub(px, py, new Vector2f());
                    Vector2f length2 = state1.getBoundingBox().getCenter().sub(px, py, new Vector2f());

                    if (length1.lengthSquared() > length2.lengthSquared()) {
                        state = state1;
                    }
                }
            }

            data = collisionBox.getCollision(state.getBoundingBox());
            if (data.isIntersecting) {
                collisionBox.correctPosition(state.getBoundingBox(), data);
                pos.set(boxToPlayer(collisionBox.getCenter(),world));
            }
        }
        System.out.println("after: " + pos);
    }
    public void collideWithTiles2(World world) {
    // Get player's position and dimensions
        int playerX = pos.getX()/32;
        int playerY = camera.getOffset().getY()/32+pos.getY()/32+Main.SCREEN_HEIGHT/64;
        int px = pos.getX();
        int py = camera.getOffset().getY()+pos.getY()+Main.SCREEN_HEIGHT/2;
        Set<TileState> tiles = new HashSet<>();
        for(int x=-16; x <= 16; x++){
            for(int y=-16; y<=16; y++){
                TileState tile = world.getTile((px+x)/32,(py+y)/32);
                tiles.add(tile);
            }
        }
        Vector2f delta = new Vector2f();
        for (TileState t : tiles) {
            //System.out.println("tile at (" + t.getBoundingBox().getCenter().x + ", " + t.getBoundingBox().getCenter().y + ") is " + t.getTile() + " and is solid: " + t.getTile().isSolid());
            Collision c = collisionBox.getCollision(t.getBoundingBox());
//            if(c.isIntersecting && t.getTile().isSolid()) delta.add(collisionBox.correctPosition(t.getBoundingBox(),c,this));
        }
        collisionBox.moveCenter(delta);
        Vector2i delta2 = new Vector2i(Math.round(delta.x),Math.round(delta.y));
        if(!delta.equals(new Vector2f(0))){
            System.out.println("final: " + delta);
            System.out.println("old pos: " + getPos());
            setPos(new Vector2i((int)collisionBox.getCenter().x, getRealY((int) collisionBox.getCenter().y,world)));
            System.out.println("new pos: " + getPos());
        }

//        for (Block block : world.getBlocks()) {
//        // Get block's position and dimensions
//        double bx = block.getX();
//        double by = block.getY();
//        double bw = block.getWidth();
//        double bh = block.getHeight();
//
//        // Check for collision using AABB (Axis-Aligned Bounding Box) method
//        if (px + pw > bx && px < bx + bw && py + ph > by && py < by + bh) {
//            // Collision detected, resolve it
//            double dx = Math.min(px + pw - bx, bx + bw - px);
//            double dy = Math.min(py + ph - by, by + bh - py);
//
//            // Resolve collision along the axis of least penetration
//            if (dx < dy) {
//                // Resolve x-axis collision
//                if (px + pw / 2 < bx + bw / 2) {
//                    // Player is to the left of the block
//                    player.setPosition(bx - pw, py);
//                } else {
//                    // Player is to the right of the block
//                    player.setPosition(bx + bw, py);
//                }
//            } else {
//                // Resolve y-axis collision
//                if (py + ph / 2 < by + bh / 2) {
//                    // Player is above the block
//                    player.setPosition(px, by - ph);
//                } else {
//                    // Player is below the block
//                    player.setPosition(px, by + bh);
//                }
//            }
//        }
//    }
    }
    public void setPos(Vector2i pos){
        this.pos.set(pos);
    }
    public int getRealY(int y, World world){
        return y-(camera.getOffset().getY() + world.getHeight()/2)/32;
    }
    public Vector2i boxToPlayer(Vector2f init, World world){
        return new Vector2i((int)init.x/32,(int)init.y/32-(camera.getOffset().getY() + world.getHeight()/2)/32);
    }
    public void addPos(Vector2i pos){
        this.pos.add(pos);
    }
    public void addPos(int x, int y){
        this.pos.add(x,y);
    }

    public Vector2i getPos(){
        return pos;
    }

    public Vector2i getRealPos(){
        return pos.copy().add(0,camera.getOffset().getY()+Main.SCREEN_HEIGHT/2).divide(32);
    }
}
