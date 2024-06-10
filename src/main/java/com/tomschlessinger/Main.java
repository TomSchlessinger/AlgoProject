package com.tomschlessinger;

import com.tomschlessinger.keybinds.KeyBindContext;
import com.tomschlessinger.keybinds.KeyBinds;
import com.tomschlessinger.render.CameraView;
import com.tomschlessinger.texture.Texture;
import com.tomschlessinger.tile.AbstractTile;
import com.tomschlessinger.tile.TileRegistry;
import com.tomschlessinger.util.Timer;
import com.tomschlessinger.util.Vector2i;
import com.tomschlessinger.world.World;
import com.tomschlessinger.world.generate.TerrainGenerator;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.*;
import java.security.Key;
import java.util.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {
    public static final int TEXTURE_SIZE = 32;//in px
//    public static int drawn = 0;
    public static Collection<AbstractTile> tiles;
    public static final int SCREEN_WIDTH = 1600;
    public static final int SCREEN_HEIGHT = 1000;
//    public static final int WORLD_WIDTH = 2000;
    public static final int WORLD_HEIGHT = 500;
    private long window;
//    private TerrainGenerator terrainGenerator;
    private World world;
    private CameraView camera;
//    public static final HashMap<AbstractTile,ByteBuffer> imageBuffers = new HashMap<>();

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        TileRegistry.init();
        world = new World(WORLD_HEIGHT);
        camera = new CameraView(world);
//        terrainGenerator = new TerrainGenerator(world, WORLD_HEIGHT, System.currentTimeMillis());
        initKeyBinds();
//        bufferImages();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(SCREEN_WIDTH, SCREEN_HEIGHT, "Hello World!", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            KeyBinds.run(key, KeyBindContext.create(window, action, scancode, mods));
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);
    }

    private void initKeyBinds() {
        KeyBinds.registerKey(GLFW_KEY_ESCAPE, context -> {
            if (context.getAction() == GLFW_RELEASE) glfwSetWindowShouldClose(context.getWindow(), true);
        });
        KeyBinds.registerKey(GLFW_KEY_D, context -> camera.move(5, 0),true);
        KeyBinds.registerKey(GLFW_KEY_A, context -> camera.move(-5, 0),true);
        KeyBinds.registerKey(GLFW_KEY_W, context -> camera.move(0, 5),true);
        KeyBinds.registerKey(GLFW_KEY_S, context -> camera.move(0, -5),true);

    }

    private void loop() {
        double counter = 0;

        glClearColor(1f, 1f, 1f, 0f);

        // Set up orthographic projection based on screen size
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glViewport(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);


        double time1 = Timer.getTime();
        while (!glfwWindowShouldClose(window)) {
            double time2 = Timer.getTime();
            double elapsed = time2-time1;
            counter++;
//            System.out.println(elapsed);
//            if((int)(elapsed)%10==0){
//                System.out.println("fps = " + frame/10d);
//                frame = 0;
//            }
            generateTerrain(camera.getWorldPos().getX());
            //printToFile(world.toString());
            //System.out.println("drawn " + drawn + " items");
            KeyBinds.pressed.forEach(
                    (key, context) -> {
                        //System.out.println(context);
                        if(context != null) {
                            //System.out.println("key " + key + " was prsesed");
                            KeyBinds.keys.get(key).forEach(c -> c.accept(context));
                        }
                    }
            );
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            render();
//            if(counter==100){
//                org.newdawn.slick.Color.white.bind();
//                time1 = time2;
//                counter=0;
//                font.drawString(50,50,"fps = " + counter/elapsed);
//            }//for fps stuff ima do in the future
            glfwSwapBuffers(window);
            glfwPollEvents();

        }
    }

    public void generateTerrain(int init){
        //Vector2i pos = camera.getWorldPos();
        world.generate(init);
    }

    private void render() {
        camera.renderWorld(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public static void main(String[] args) {
        new Main().run();
    }
    public static void printToFile(String s){
        File file = new File("world");
        try{
            file.createNewFile();
            FileWriter writer = new FileWriter("world");
            writer.write(s);
        } catch (IOException e) {
            System.out.println("blah");
        }
    }

}
