package com.tomschlessinger.player;

import com.tomschlessinger.Main;
import com.tomschlessinger.render.CameraView;
import com.tomschlessinger.texture.Texture;
import com.tomschlessinger.tile.TileRegistry;
import com.tomschlessinger.util.Vector2i;
import com.tomschlessinger.world.World;

import java.util.Vector;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_QUADS;

public class Player {
    private final Vector2i pos;
    private final Vector2i vel;
    private final Vector2i acc;
    private final Texture texture;
    private final World world;
    private final CameraView camera;

    public Player(CameraView cam, String textureFile, World world){
        this.camera=cam;
        this.world=world;
        texture = new Texture("/assets/textures/player/" + textureFile);
        pos = Vector2i.ZERO.copy();
        vel = Vector2i.ZERO.copy();
        acc = Vector2i.ZERO.copy();
    }
    public void tick(){
//        System.out.println("player pos: " + pos);
//        System.out.println("camera pos: " + camera);
        handleCollisions();
        pos.add(vel);
        camera.move(vel);
        vel.add(acc);
        vel.set(Vector2i.ZERO);
    }
    public void move(Vector2i impulse){//more like delta v but who caresssss
        vel.add(impulse);
    }
    public void setVel(Vector2i vel){
        this.vel.set(vel);
    }
    public void setVel(int x, int y){
        vel.setX(x);
        vel.setY(y);
    }
    public void move(int dx, int dy){
        vel.add(dx,dy);
    }
    public void accelerate(int vX, int vY){
        this.acc.add(vX,vY);
    }
    public void handleCollisions(){
        Vector2i pos = camera.getRealPos().divide(32);
        int vX = vel.getX();
        int vY = vel.getY();
        int newX = pos.getX()+vX;
        int newY = pos.getY()+vY;
//        System.out.println("im  currently at " + pos + "which is " + world.getTile(newX,newY));
//        System.out.println("i will be at " + new Vector2i(newX,newY) + " which is " + world.getTile(newX,newY));
        //System.out.println("next coords" + new Vector2i(newX, newY));
        System.out.println(newX + ", " + newY + " is air: " + world.isAir(newX,newY));
        if(!world.isAir(newX,newY) || !world.isAir(pos)){
//            vel.set(Vector2i.ZERO);
//            acc.set(Vector2i.ZERO);
            System.out.println("colliding");
            boolean foundX = true;
            boolean foundY = true;
            System.out.println("old vel: " + vel);
            for(int x = 0; x < vX; x++){
                for(int y = 0; y < vY; y++){
                    newX = pos.getX()+x;
                    newY = pos.getY()+y;
                    if(foundX && !world.isAir(newX,newY)) {
                        foundX = false;
                        vel.setX(0);
                        acc.setX(0);
                    }
                    if(foundY && !world.isAir(newX,newY)) {
                        foundY = false;
                        vel.setY(0);
                        acc.setY(0);
                    }
                }
            }

            System.out.println("new vel: " + vel);

            if(foundX) {
                vel.setX(0);
                acc.setX(0);
            }
            if(foundY) {
                vel.setY(0);
                acc.setY(0);
            }
        }
    }
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


}
