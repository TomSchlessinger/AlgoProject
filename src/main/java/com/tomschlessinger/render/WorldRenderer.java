package com.tomschlessinger.render;

import com.tomschlessinger.tile.AbstractTile;
import com.tomschlessinger.tile.TileRegistry;
import com.tomschlessinger.util.Pixel;
import com.tomschlessinger.util.TextureLoader;
import com.tomschlessinger.util.Vector2i;
import com.tomschlessinger.world.World;
import org.w3c.dom.css.RGBColor;

public class WorldRenderer {
    private final World world;
    public WorldRenderer(World world){
           this.world = world;
    }
    public Pixel getPixel(Vector2i pos){
        AbstractTile tile = world.getTile(pos.getX()/8,pos.getY()/8);
        if(tile == TileRegistry.getTile("air"))return new Pixel(-1,-1,-1);//air
        int pixel = TextureLoader.loadImage(tile.getTexturePath()).getRGB(pos.getX()%8,pos.getY()%8);
        int r = (byte) ((pixel >> 16) & 0xFF);
        int g = (byte) ((pixel >> 8) & 0xFF);
        int b = (byte) (pixel & 0xFF);

        //System.out.println("pixel at " + pos + " is " + r + ", " +g + ", " + b);
        return new Pixel(r,g,b);
    }
}


