package com.tomschlessinger;

import com.tomschlessinger.keybinds.KeyBindContext;
import com.tomschlessinger.keybinds.KeyBinds;
import com.tomschlessinger.render.CameraView;
import com.tomschlessinger.world.World;
import com.tomschlessinger.world.generate.TerrainGenerator;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    private static final int SCREEN_WIDTH = 1600;
    private static final int SCREEN_HEIGHT = 1000;
    private static final int WORLD_WIDTH = 200;
    private static final int WORLD_HEIGHT = 50;

    private long window;
    private TerrainGenerator terrainGenerator;
    private final World world = new World(WORLD_WIDTH, WORLD_HEIGHT);
    private final CameraView camera = new CameraView(world);

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        initKeyBinds();
        generateTerrain();
        System.out.println(world);
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
    }

    private void initKeyBinds() {
        KeyBinds.registerKey(GLFW_KEY_ESCAPE, context -> {
            if (context.getAction() == GLFW_RELEASE) glfwSetWindowShouldClose(context.getWindow(), true);
        });
        KeyBinds.registerKey(GLFW_KEY_RIGHT, context -> {
            if (context.getAction() == GLFW_RELEASE) {
                camera.move(8, 0);
            }
        });
        KeyBinds.registerKey(GLFW_KEY_LEFT, context -> {
            if (context.getAction() == GLFW_RELEASE) {
                camera.move(-8, 0);
            }
        });
        KeyBinds.registerKey(GLFW_KEY_UP, context -> {
            if (context.getAction() == GLFW_RELEASE) {
                camera.move(0, 8);
            }
        });
        KeyBinds.registerKey(GLFW_KEY_DOWN, context -> {
            if (context.getAction() == GLFW_RELEASE) {
                camera.move(0, -8);
            }
        });
    }

    private void generateTerrain() {
        terrainGenerator = new TerrainGenerator(WORLD_WIDTH, WORLD_HEIGHT, System.currentTimeMillis());
        terrainGenerator.generateSurface(world);
    }

    private void loop() {
        GL.createCapabilities();
        glClearColor(1f, 1f, 1f, 0f);

        // Set up orthographic projection based on screen size
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glViewport(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            render();

            glfwSwapBuffers(window);
            glfwPollEvents();

            checkGLError();
        }
    }

    private void render() {
        camera.renderWorld(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    private void checkGLError() {
        int error;
        while ((error = glGetError()) != GL_NO_ERROR) {
            System.err.println("OpenGL Error: " + error);
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}

//package com.tomschlessinger;
//
//import com.tomschlessinger.keybinds.KeyBindContext;
//import com.tomschlessinger.keybinds.KeyBinds;
//import com.tomschlessinger.render.CameraView;
//import com.tomschlessinger.world.World;
//import com.tomschlessinger.world.generate.TerrainGenerator;
//import org.lwjgl.*;
//import org.lwjgl.glfw.*;
//import org.lwjgl.opengl.*;
//import org.lwjgl.system.*;
//
//import java.nio.*;
//
//import static org.lwjgl.glfw.Callbacks.*;
//import static org.lwjgl.glfw.GLFW.*;
//import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.system.MemoryStack.*;
//import static org.lwjgl.system.MemoryUtil.*;
//
//public class Main {
//
//    private static final int SCREEN_WIDTH = 1600;
//    private static final int SCREEN_HEIGHT = 1000;
//    private static final int WORLD_WIDTH = 200;
//    private static final int WORLD_HEIGHT = 50;
//
//    private long window;
//    private TerrainGenerator terrainGenerator;
//    private float[][] heightMap;
//    private final World world = new World(WORLD_WIDTH,WORLD_HEIGHT);
//    private final CameraView camera = new CameraView(world);
//    public void run() {
//        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
//
//        init();
//        initKeyBinds();
//        generateTerrain();
//        System.out.println(world);
//        loop();
//
//        glfwFreeCallbacks(window);
//        glfwDestroyWindow(window);
//
//        glfwTerminate();
//        glfwSetErrorCallback(null).free();
//    }
//
//    private void init() {
//        GLFWErrorCallback.createPrint(System.err).set();
//
//        if (!glfwInit())
//            throw new IllegalStateException("Unable to initialize GLFW");
//
//        glfwDefaultWindowHints();
//        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
//        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
//
//        window = glfwCreateWindow(SCREEN_WIDTH, SCREEN_HEIGHT, "Hello World!", NULL, NULL);
//        if (window == NULL)
//            throw new RuntimeException("Failed to create the GLFW window");
//
//        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
//            KeyBinds.run(key, KeyBindContext.create(window, action, scancode, mods));
//            //System.out.println(GLFW.glfwGetKeyName(key, scancode) + " was pressed");
//        });
//
//        try (MemoryStack stack = stackPush()) {
//            IntBuffer pWidth = stack.mallocInt(1);
//            IntBuffer pHeight = stack.mallocInt(1);
//
//            glfwGetWindowSize(window, pWidth, pHeight);
//
//            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
//
//            glfwSetWindowPos(
//                    window,
//                    (vidmode.width() - pWidth.get(0)) / 2,
//                    (vidmode.height() - pHeight.get(0)) / 2
//            );
//        }
//
//        glfwMakeContextCurrent(window);
//        glfwSwapInterval(1);
//        glfwShowWindow(window);
//    }
//
//    private void initKeyBinds() {
//        KeyBinds.registerKey(GLFW_KEY_ESCAPE, context -> {
//            if (context.getAction() == GLFW_RELEASE) glfwSetWindowShouldClose(context.getWindow(), true);
//        });
//        KeyBinds.registerKey(GLFW_KEY_RIGHT, context -> {
//            if (context.getAction() == GLFW_RELEASE) {
//
//                camera.move(1,0);
//                //generateTerrain();
//            }
//        });
//
//        KeyBinds.registerKey(GLFW_KEY_LEFT, context -> {
//            if (context.getAction() == GLFW_RELEASE) {
//                camera.move(-1,0);
//                //generateTerrain();
//            }
//        });
//
//        KeyBinds.registerKey(GLFW_KEY_UP, context -> {
//            if (context.getAction() == GLFW_RELEASE) {
//                camera.move(0,1);
//                //generateTerrain();
//            }
//        });
//
//        KeyBinds.registerKey(GLFW_KEY_DOWN, context -> {
//            if (context.getAction() == GLFW_RELEASE) {
//                camera.move(0,-1);
//                //generateTerrain();
//            }
//        });
//    }
//
//    private void generateTerrain() {
//        terrainGenerator = new TerrainGenerator(WORLD_WIDTH,WORLD_HEIGHT,System.currentTimeMillis());
//        terrainGenerator.generateSurface(world);
////        terrainGenerator = new TerrainGenerator(100, 100, System.currentTimeMillis());
////        heightMap = terrainGenerator.generateTerrain();
////        // Scale height values to ensure they fall within a visible range
////        for (int x = 0; x < heightMap.length; x++) {
////            for (int y = 0; y < heightMap[0].length; y++) {
////                heightMap[x][y] *= 10;  // Scale height values for visibility
////            }
////        }
//    }
//
//    private void loop() {
//        GL.createCapabilities();
//        glClearColor(1f, 1f, 1f, 0f);
//
//        // Set up orthographic projection based on screen size
//        glMatrixMode(GL_PROJECTION);
//        glLoadIdentity();
//        glOrtho(0, SCREEN_WIDTH, 0, SCREEN_HEIGHT, -1, 1);
//        glMatrixMode(GL_MODELVIEW);
//        glLoadIdentity();
//
//        while (!glfwWindowShouldClose(window)) {
//            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//
//            render();
//
//            glfwSwapBuffers(window);
//            glfwPollEvents();
//        }
//    }
//
//
//    private void render() {
//        camera.renderWorld(SCREEN_WIDTH,SCREEN_HEIGHT);
//
//    }
//
//    public static void main(String[] args) {
//        new Main().run();
//    }
//}
