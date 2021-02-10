package io.innocent.dream.actions;

import io.innocent.dream.InnocentDream;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotAction extends AbstractAction {

    public boolean ready = false;

    public ScreenshotAction() {
        super(GLFW.GLFW_KEY_F2);
    }

    @Override
    public void preformAction() {
        if (!ready) ready = true;
    }

    @Override
    public void afterActionPreformed() {
        if (ready) {
            GL11.glReadBuffer(GL11.GL_FRONT);
            int width = InnocentDream.WIDTH;
            int height = InnocentDream.HEIGHT;
            int bpp = 4;
            ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
            GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );

            testDir();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yy-HH-mm-ss");
            String fileName = LocalDateTime.now().format(formatter) + ".png";
            File file = new File( "screenshots\\" + fileName);
            String format = "PNG";
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
            try {
                ImageIO.write(image, format, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Took Screenshot " + fileName);
        }
        ready = false;
    }

    private static void testDir() {
        File screenshotDir = new File("screenshots");
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
    }

}
