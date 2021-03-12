package io.innocent.dream.drawing;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.util.DisplayManager;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.opengl.GL11.*;

public class TextureRenderer {

    public static final Texture[] CHARACTERS = new Texture[128];
    public static final InputStream UNKNOWN_TEXTURE;

    static {
        InputStream UNKNOWN_TEXTURE1;
        for (char i = 0; i < CHARACTERS.length; i++) {
            CHARACTERS[i] = generateCharTex(i);
        }
        try {
            UNKNOWN_TEXTURE1 = InnocentDream.getResAsStream("assets/unknown_texture.png");
        } catch (FileNotFoundException e) {
            UNKNOWN_TEXTURE1 = null;
            e.printStackTrace();
        }
        UNKNOWN_TEXTURE = UNKNOWN_TEXTURE1;
    }

    public static int loadTexture(String path) {
        glfwMakeContextCurrent(DisplayManager.win);

        int[] pixels = null;
        int width = 0;
        int height = 0;
        BufferedImage image;
        try {
            InputStream resourceBuff = InnocentDream.getResAsStream(path);
            image = ImageIO.read(resourceBuff);
        } catch (Exception e) {
            try {
                image = ImageIO.read(UNKNOWN_TEXTURE);
            } catch (IOException ioException) {
                image = null;
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
        width = image.getWidth();
        height = image.getHeight();
        pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff000) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        ByteBuffer dataBuff = BufferUtils.createByteBuffer(width * height * 4);
        dataBuff.asIntBuffer().put(data);

        int result = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, result);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                dataBuff);
        return result;
    }

    public static int loadTextureFromSectionOfImage(String path, int x, int y, int w, int h) {
        glfwMakeContextCurrent(DisplayManager.win);

        int[] pixels = null;
        int width = 0;
        int height = 0;
        InputStream resourceBuff;
        try {
            resourceBuff = InnocentDream.getResAsStream(path);
        } catch (Exception e) {
            resourceBuff = UNKNOWN_TEXTURE;
            e.printStackTrace();
        }
        try {
            BufferedImage image = ImageIO.read(resourceBuff).getSubimage(x, y, w, h);
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff000) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        ByteBuffer dataBuff = BufferUtils.createByteBuffer(width * height * 4);
        dataBuff.asIntBuffer().put(data);

        int result = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, result);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                dataBuff);
        return result;
    }

    public static int loadTexIDFromImage(BufferedImage image) {
        int[] pixels = null;
        int width = 0;
        int height = 0;
        width = image.getWidth();
        height = image.getHeight();
        pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff000) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        ByteBuffer dataBuff = BufferUtils.createByteBuffer(width * height * 4);
        dataBuff.asIntBuffer().put(data);

        int result = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, result);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                dataBuff);
        return result;
    }

    public static Texture getCharacter(char c) {
        return CHARACTERS[c];
    }

    public static Texture generateCharTex(char c) {
        int i = 0;
        for (int y = 0; y < 256; y += Text.HEIGHT) {
            for (int x = 0; x < 256; x += Text.WIDTH) {
                if (i == c) {
                    return new Texture("assets/fonts/ascii.png", x, y, Text.WIDTH, Text.HEIGHT);
                }
                i++;
            }
        }
        return null;
    }

}
