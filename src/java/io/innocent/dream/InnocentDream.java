package io.innocent.dream;

import io.innocent.dream.actions.Actions;
import io.innocent.dream.tile.Tiles;
import io.innocent.dream.util.GameAPIManager;
import io.innocent.dream.util.IOUtil;
import io.innocent.dream.util.LibraryManager;
import io.innocent.dream.worldBuilder.WorldBuilder;
import io.innocent.dream.worldBuilder.WorldTileManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryUtil.memAllocInt;

public class InnocentDream implements Runnable {

    public static final GameAPIManager apiManager;
    public static final String version = "0.0.1";

    static {
        apiManager = new GameAPIManager();
    }

    public static long win = 0L;
    public static int WIDTH = 1280;
    public static int HEIGHT = 640;

    @Override
    public void run() {
        init();
        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(-256, 256, -128, 128, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        Tiles.init();
        Actions.init();
        WorldBuilder.generateWorld();
        while (!GLFW.glfwWindowShouldClose(win)) {
            Actions.pollActions();
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);
            WorldTileManager.renderWorld();
            for (int i = -256; i < 257; i += 16) {
                WorldTileManager.setTileAtPos("grass", i, -32);
            }

            glfwSwapBuffers(win);
        }
        destroy();
    }

    public static InputStream getResAsStream(String path) throws FileNotFoundException {
        ClassLoader loader = InnocentDream.class.getClassLoader();
        InputStream in = loader.getResourceAsStream(path);
        if (in == null) {
            throw new FileNotFoundException(path);
        }
        return in;
    }

    public void init() {
        if (!GLFW.glfwInit()) {
            System.err.println("Failed to Initialize GLFW");
            System.exit(1);
        }
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        win = glfwCreateWindow(1280, 640, "Window", 0, 0);
        ByteBuffer icon16;
        ByteBuffer icon32;
        try {
            icon16 = IOUtil.ioResourceToByteBuffer("assets/images/icon-16.png", 2048);
            icon32 = IOUtil.ioResourceToByteBuffer("assets/images/icon-32.png", 4096);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        IntBuffer w = memAllocInt(1);
        IntBuffer h = memAllocInt(1);
        IntBuffer comp = memAllocInt(1);
        try ( GLFWImage.Buffer icons = GLFWImage.malloc(2) ) {
            ByteBuffer pixels16 = stbi_load_from_memory(icon16, w, h, comp, 4);
            icons.position(0)
                    .width(w.get(0))
                    .height(h.get(0))
                    .pixels(pixels16);
            ByteBuffer pixels32 = stbi_load_from_memory(icon32, w, h, comp, 4);
            icons.position(1)
                    .width(w.get(0))
                    .height(h.get(0))
                    .pixels(pixels32);
            icons.position(0);
            glfwSetWindowIcon(win, icons);
            stbi_image_free(pixels32);
            stbi_image_free(pixels16);
        }
        glfwSetWindowTitle(win, "Innocent Dream " + InnocentDream.version);

        glfwSetWindowSizeCallback(win, new GLFWWindowSizeCallback() {
            @Override public void invoke(long window, int argWidth, int argHeight) {
                resizeWindow(argWidth, argHeight);
            }
            private void resizeWindow(int argWidth, int argHeight) {
                glViewport(0, 0, argWidth,argHeight);
                WIDTH = argWidth;
                HEIGHT = argHeight;
            }
        });
        glfwMakeContextCurrent(win);
        GL.createCapabilities();
        glfwMakeContextCurrent(win);
        glfwShowWindow(win);
    }

    public void destroy() {
        glfwDestroyWindow(win);
    }

    public static void main(String[] args) {
        LibraryManager.testLibs();
        System.setProperty("org.lwjgl.librarypath",
                "C:\\Users\\jdv20\\dev\\JAVA\\innocent-dream\\libs\\natives");
        new Thread(new InnocentDream()).start();
    }

}
