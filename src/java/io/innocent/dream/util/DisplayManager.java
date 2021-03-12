package io.innocent.dream.util;

import io.innocent.dream.InnocentDream;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import sun.awt.image.ToolkitImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryUtil.memAllocInt;

public class DisplayManager {

    public static long win;
    public static int WIDTH;
    public static int HEIGHT;

    public static void create() {
        if (!GLFW.glfwInit()) {
            System.err.println("Failed to Initialize GLFW");
            System.exit(1);
        }
        Settings.start();

        boolean fullscreen = Boolean.parseBoolean((String) Settings.getSetting("fullscreen"));

        //Getting Monitor
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode mode = glfwGetVideoMode(monitor);

        //Setting up Fullscreen
        if (fullscreen) {
            glfwWindowHint(GLFW_RED_BITS, mode.redBits());
            glfwWindowHint(GLFW_GREEN_BITS, mode.greenBits());
            glfwWindowHint(GLFW_BLUE_BITS, mode.blueBits());
            glfwWindowHint(GLFW_REFRESH_RATE, mode.refreshRate());
        }

        //The Window Handle
        if (fullscreen) {
            WIDTH = mode.width();
            HEIGHT = mode.height();
            win = glfwCreateWindow(WIDTH, HEIGHT, "Window", monitor, 0);
        } else {
            float scale = Float.parseFloat((String) Settings.getSetting("scale"));
            WIDTH = (int) (1280 * scale);
            HEIGHT = (int) (640 * scale);
            win = glfwCreateWindow(WIDTH, HEIGHT, "Window", 0, 0);
        }

        //Icon Setting
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

        //Mouse Cursor
        InputStream stream = null;
        ToolkitImage image = null;
        try {
            stream = InnocentDream.getResAsStream("assets/icon-map.png");
            int wi = mode.width();
            int he = mode.height();
            BufferedImage img = ImageIO.read(stream).getSubimage(16, 16, 112, 112);
            image = (ToolkitImage) img
                    .getScaledInstance(wi / 32, he / 18, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }


        int width = image.getWidth();
        int height = image.getHeight();

        int[] pixels = new int[width * height];
        image.getBufferedImage().getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int pixel = pixels[y * width + x];

                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        GLFWImage cursorImg= GLFWImage.create();
        cursorImg.width(width);
        cursorImg.height(height);
        cursorImg.pixels(buffer);

        int hotspotX = 3;
        int hotspotY = 6;

        long cursorID = GLFW.glfwCreateCursor(cursorImg, hotspotX , hotspotY);

        glfwSetCursor(win, cursorID);

        //Setting window title
        glfwSetWindowTitle(win, "Innocent Dream " + InnocentDream.version);

        //Window size changing
        glfwSetWindowSizeCallback(win, new GLFWWindowSizeCallback() {
            @Override public void invoke(long window, int argWidth, int argHeight) {
                resizeWindow(argWidth, argHeight);
                WIDTH = argWidth;
                HEIGHT = argHeight;
                InnocentDream.calculateHeightScale();
            }
            private void resizeWindow(int argWidth, int argHeight) {
                glViewport(0, 0, argWidth,argHeight);
            }
        });

        //Getting Ready to Draw Everything
        glfwMakeContextCurrent(win);
        GL.createCapabilities();
        glfwMakeContextCurrent(win);
        glfwShowWindow(win);
    }

    public static void goFullScreen() {
        glfwMakeContextCurrent(win);
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode mode = glfwGetVideoMode(monitor);
        WIDTH = mode.width();
        HEIGHT = mode.height();
        glfwSetWindowMonitor(win, monitor, 0, 0, WIDTH, HEIGHT, mode.refreshRate());
        InnocentDream.calculateHeightScale();
    }

    public static void goWindowed() {
        glfwMakeContextCurrent(win);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        float scale = Float.parseFloat((String) Settings.getSetting("scale"));
        WIDTH = (int) (1280 * scale);
        HEIGHT = (int) (640 * scale);
        int x = dim.width / 2 - WIDTH / 2;
        int y = dim.height / 2 - HEIGHT / 2;
        glfwSetWindowMonitor(win, 0, x, y, WIDTH, HEIGHT, 1);
        InnocentDream.calculateHeightScale();
    }

    public static void update() {
        glfwMakeContextCurrent(win);
        glfwPollEvents();

        glClear(GL_COLOR_BUFFER_BIT);

        glfwSwapBuffers(win);

        InnocentDream.timer.updateTime();
    }

    public static float[] getMousePositionRelativeToScreen() {
        DoubleBuffer xCoords = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yCoords = BufferUtils.createDoubleBuffer(1);
        IntBuffer winWidth = BufferUtils.createIntBuffer(1);
        IntBuffer winHeight = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(DisplayManager.win, winWidth, winHeight);
        glfwGetCursorPos(DisplayManager.win, xCoords, yCoords);
        float x = (float) xCoords.get(0);
        float y = (float) yCoords.get(0);
        float scaleX = 512 / (float) (winWidth.get(0));
        float scaleY = 256 / (float) (winHeight.get(0));
        x *= scaleX;
        y *= -scaleY;
        x -= 256f;
        y += 128f;
        return new float[]{x, y};
    }

}
