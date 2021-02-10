package io.innocent.dream.tile;

import io.innocent.dream.drawing.Texture;

import static org.lwjgl.opengl.GL11.*;

public class Tile {

    public enum Type {
        AIR,
        WALL,
        BLOCK,
        LIQUID
    }

    public static final float TILE_WIDTH = 16f;
    protected final Type type;
    protected final Texture texture;

    public Tile(Type type, Texture texture) {
        this.type = type;
        this.texture = texture;
    }

    public Type getType() {
        return type;
    }

    public Texture getTexture() {
        return texture;
    }

    public void renderTileAtPos(float x, float y) {
        float TILE_WIDTH = Tile.TILE_WIDTH / 2f;
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        glBegin(GL_QUADS);
        glTexCoord2f(1, 0);
        glVertex2f(-TILE_WIDTH + x, TILE_WIDTH + y);
        glTexCoord2f(0, 0);
        glVertex2f(TILE_WIDTH + x, TILE_WIDTH + y);
        glTexCoord2f(0, 1);
        glVertex2f(TILE_WIDTH + x, -TILE_WIDTH + y);
        glTexCoord2f(1, 1);
        glVertex2f(-TILE_WIDTH + x, -TILE_WIDTH + y);
        glEnd();
        glDisable(GL_TEXTURE_2D);
    }

}
