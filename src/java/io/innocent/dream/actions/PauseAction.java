package io.innocent.dream.actions;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.drawing.TextureRenderer;
import io.innocent.dream.util.DisplayManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class PauseAction extends Action {

    public PauseAction() {
        super("key_pause");
    }

    @Override
    public void preformAction() {
        GL11.glReadBuffer(GL11.GL_FRONT);
        int width = DisplayManager.WIDTH;
        int height = DisplayManager.HEIGHT;
        int bpp = 4;
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }

        InnocentDream.PAUSE_MENU.setBackground(TextureRenderer.loadTexIDFromImage(image));
        InnocentDream.CURRENT_SCREEN = InnocentDream.PAUSE_MENU;
    }

}
