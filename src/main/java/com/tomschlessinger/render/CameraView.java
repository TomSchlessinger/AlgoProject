package com.tomschlessinger.render;

import com.tomschlessinger.tile.AbstractTile;
import com.tomschlessinger.util.Pixel;
import com.tomschlessinger.util.Vector2i;
import com.tomschlessinger.world.World;

import static org.lwjgl.opengl.GL11.*;

public class CameraView {
    private int x;
    private int y;
    private float zoom = 1;
    private final World world;
    private final WorldRenderer renderer;

    public CameraView(World world) {
        this.world = world;
        this.renderer = new WorldRenderer(world);
        this.x = world.getDims().getX() / 2;
        this.y = 100; // Initial camera position
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
        System.out.println("camera pos: (" + x + ", " + y + ")");
    }

    public void zoom(float delta) {
        this.zoom += delta;
    }

    public void renderWorld(int width, int height) {
        glBegin(GL_QUADS);
        for (int i = 0; i < width; i += 8) {
            for (int j = 0; j < height; j += 8) {
                Vector2i pos = new Vector2i(i + this.x, j + this.y);
                Pixel p = renderer.getPixel(pos.divide(8));
                if (!(p.getBlue() == -1 && p.getRed() == -1 && p.getGreen() == -1)) {
                    glColor3f(p.getRed() / 255.0f, p.getGreen() / 255.0f, p.getBlue() / 255.0f);
                    glVertex2f(i, j);
                    glVertex2f(i + 8, j);
                    glVertex2f(i + 8, j + 8);
                    glVertex2f(i, j + 8);
                }
            }
        }
        glEnd();
    }
}

//package com.tomschlessinger.render;
//
//import com.tomschlessinger.tile.AbstractTile;
//import com.tomschlessinger.util.Pixel;
//import com.tomschlessinger.util.Vector2i;
//import com.tomschlessinger.world.World;
//
//import static org.lwjgl.opengl.GL11.*;
//
//public class CameraView {
//    private int x;
//    private int y;
//    private float zoom = 1;
//    private final World world;
//    private final WorldRenderer renderer;
//    public CameraView(World world){
//        this.world=world;
//        renderer = new WorldRenderer(world);
//        x = world.getDims().getX()/2;
//        y = 100;//world.getDims().getY()/2 * 0;
//    }
//    public void move(int dx, int dy){
//        x+=dx;
//        y+=dy;
//        System.out.println("camera pos: (" + x + ", " + y+")");
//    }
//    public void zoom(float delta){
//        this.zoom+=delta;
//    }
//    public void renderWorld(int width, int height){
////        int worldX = world.getDims().x() * 8;
////        int worldY = world.getDims().y() * 8;
//        glBegin(GL_QUADS);
//        for(int x = 0; x < width; x++){
//            for(int y = 0; y < height; y++){
//                Vector2i pos = new Vector2i(x+this.x,y+this.y);
//                Pixel p = renderer.getPixel(pos);
//                if(!(p.getBlue() == -1 && p.getRed() == -1 && p.getGreen() == -1)) {
//                    glColor3i(
//                            p.getRed(),
//                            p.getGreen(),
//                            p.getBlue()
//                    );
//                    glVertex2f(pos.getX(), pos.getY());
////                    glVertex2f(pos.getX()+1, pos.getY());
////                    glVertex2f(pos.getX(), pos.getY()+1);
////                    glVertex2f(pos.getX()+1, pos.getY()+1);
//
//                }
//            }
//        }
////        for (int x = 0; x < heightMap.length - 1; x++) {
////            for (int y = 0; y < heightMap[0].length - 1; y++) {
////                float h1 = heightMap[x][y];
////                float h2 = heightMap[x + 1][y];
////                float h3 = heightMap[x + 1][y + 1];
////                float h4 = heightMap[x][y + 1];
////
////                glColor3f(h1 / 10, h1 / 10, h1 / 10); // Adjust color scaling for visibility
////                glVertex2f(x, y);
////                glColor3f(h2 / 10, h2 / 10, h2 / 10);
////                glVertex2f(x + 1, y);
////                glColor3f(h3 / 10, h3 / 10, h3 / 10);
////                glVertex2f(x + 1, y + 1);
////                glColor3f(h4 / 10, h4 / 10, h4 / 10);
////                glVertex2f(x, y + 1);
////            }
////        }
//        glEnd();
//    }
//}
