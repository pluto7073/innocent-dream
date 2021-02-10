package io.innocent.dream.drawing;

import io.innocent.dream.InnocentDream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnimatedTexture extends Texture {

    private final int width;
    private final int height;
    private List<Frame> frames;

    public AnimatedTexture(String texturePath, int width, int height) {
        super(texturePath);
        this.width = width;
        this.height = height;
        frames = new ArrayList<>();
    }

    @Override
    protected void generateTexture() throws IOException {
        BufferedImage texture = ImageIO.read(InnocentDream.getResAsStream(getTexturePath()));
        for (int y = 0; y < texture.getHeight(); y += height) {
            for (int x = 0; x < texture.getWidth(); x += width) {

            }
        }
    }

    public class Frame extends Texture {
        private int x;
        private int y;

        public Frame(String texturePath, int x, int y) {
            super(texturePath);
            this.x = x;
            this.y = y;
        }

        @Override
        protected void generateTexture() throws IOException {

        }
    }

}
