package io.innocent.dream.drawing;

import io.innocent.dream.InnocentDream;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.opengl.GL11.*;

class TextureRenderer {

    public static int loadTexture(String path) {
        glfwMakeContextCurrent(InnocentDream.win);

        int[] pixels = null;
        int width = 0;
        int height = 0;
        try {
            InputStream resourceBuff = InnocentDream.getResAsStream(path);
            BufferedImage image = ImageIO.read(resourceBuff);
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (Exception e) {
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

    public static int loadTextureFromSectionOfImage(String path, int x, int y, int w, int h) {
        glfwMakeContextCurrent(InnocentDream.win);

        int[] pixels = null;
        int width = 0;
        int height = 0;
        try {
            InputStream resourceBuff = InnocentDream.getResAsStream(path);
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

}
