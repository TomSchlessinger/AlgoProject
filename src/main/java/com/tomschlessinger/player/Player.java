package com.tomschlessinger.player;

import com.tomschlessinger.Main;
import com.tomschlessinger.texture.Texture;
import com.tomschlessinger.util.Vector2i;
import com.tomschlessinger.world.World;

import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Player {
    public final Vector2i pos;
    public final Vector2i vel;
    public final Vector2i acc;
    public final Texture texture;
    public final World world;

    public Player(String textureFile, World world){
        this.world=world;
        texture = new Texture("/assets/textures/player/" + textureFile);
        pos = Vector2i.ZERO.copy();
        vel = Vector2i.ZERO.copy();
        acc = Vector2i.ZERO.copy();
    }
    public void tick(){
        pos.add(vel);
        vel.add(acc);
    }
    public void move(Vector2i impulse){//more like delta v but who caresssss
        vel.add(impulse);
    }
    public void handleCollisions(){

    }
    public double distanceTo(Vector2i v){
        return pos.copy().subtract(v).normalize();
    }
    public void render(){
        texture.bind();
        float x = texture.getWidth();
        float y = texture.getHeight();

        glTexCoord2f(0, 0);
        glVertex2f(-x/2, -y/2);

        glTexCoord2f(1, 0);
        glVertex2f(x/2, -y/2);

        glTexCoord2f(1, 1);
        glVertex2f(x/2, y/2);

        glTexCoord2f(0, 1);
        glVertex2f(-x/2, y/2);
    }
}
