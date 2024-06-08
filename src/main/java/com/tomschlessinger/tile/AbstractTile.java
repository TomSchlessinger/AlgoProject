package com.tomschlessinger.tile;


import org.lwjgl.openvr.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class AbstractTile {
    private final String texture;
    private final String id;
    public AbstractTile(String s, String texturePath){
        texture = texturePath;
        id=s;
    }
    public String getId(){return id;}
    public String getTexturePath(){return "/assets/textures/tiles/" + texture;}
    public static BufferedImage getImage(AbstractTile tile){
        try {
            BufferedImage image = ImageIO.read(new File("assets/textures/tiles/" + tile.getTexturePath()));
            return image;
        } catch (IOException e) {
            System.out.println("Could not load texture");
        }
        return null;
    }

    public String toString(){
        return String.copyValueOf(new char[]{this.id.toCharArray()[0]});
    }
}
