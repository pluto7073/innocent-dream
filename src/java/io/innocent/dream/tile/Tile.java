package io.innocent.dream.tile;

import io.innocent.dream.drawing.Texture;
import org.joml.Vector2f;

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
    protected final ItemDropTable dropTable;

    public Tile(Type type, Texture texture) {
        this.type = type;
        this.texture = texture;
        this.dropTable = new ItemDropTable("data/tiles/air.iddt");
    }

    public Tile(Type type, Texture texture, String dropTablePath) {
        this.type = type;
        this.texture = texture;
        this.dropTable = new ItemDropTable(dropTablePath);
    }

    public Type getType() {
        return type;
    }

    public Texture getTexture() {
        return texture;
    }

    public void onPlace(Vector2f position) {}
    
    public void onUse(Vector2f position) {}

    public void onDestroy(Vector2f position) {
        dropTable.spawnItems(position);
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
