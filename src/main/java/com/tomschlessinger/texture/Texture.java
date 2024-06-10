package com.tomschlessinger.texture;

import com.tomschlessinger.Main;
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL12;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Texture {
    private int id;
    private int width;
    private int height;

    public Texture(String filename) {
        int bytes = 4;
        if(filename == null)return;
        BufferedImage image;
        try {
            //TextureLoader.getTexture("png",Main.class.getResource(filename).openStream());
            image = ImageIO.read(Main.class.getResource(filename));
            width = image.getWidth();
            height = image.getHeight();
            int[] pixels = new int[image.getWidth() * image.getHeight()];
            image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

            ByteBuffer buf = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * bytes); //4 for RGBA, 3 for RGB

            for(int y = 0; y < image.getHeight(); y++){
                for(int x = 0; x < image.getWidth(); x++){
                    int pixel = pixels[y * image.getWidth() + x];
                    buf.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                    buf.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                    buf.put((byte) (pixel & 0xFF));               // Blue component
                    buf.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
                }
            }
            buf.flip();

            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

            //Setup texture scaling filtering
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            //Send texel data to OpenGL
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
//            switch(bytes){
//                case 4 -> glTexImage2D(GL_TEXTURE_2D, 0,GL_RGBA , width, height,0, GL_RGBA, GL_BYTE, pixels);
//                case 3 -> glTexImage2D(GL_TEXTURE_2D, 0,GL_RGB , width, height,0, GL_RGB, GL_BYTE, pixels);
//            }
            int error = glGetError();
            if (error != GL_NO_ERROR) {
                System.err.println("Texture creation error: " + error);
            }
        } catch (IOException e){
            System.out.println("the image wasn't found at " + filename);
        }
        System.out.println("file " + filename + " has an id of " + id);

    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D,id);
    }

    public int getId() {
        return id;
    }

    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }


}
