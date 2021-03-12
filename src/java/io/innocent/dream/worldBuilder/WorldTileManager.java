package io.innocent.dream.worldBuilder;

import io.innocent.dream.entities.Entities;
import io.innocent.dream.registry.Registry;
import io.innocent.dream.tile.Tile;
import io.innocent.dream.util.DisplayManager;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class WorldTileManager {

    public static final int WORLD_SIZE_X = 256;
    public static final int WORLD_SIZE_Y = 128;
    public static final int MIN_X = -(WORLD_SIZE_X / 2);
    public static final int MAX_X = WORLD_SIZE_X / 2 - 1;
    public static final int MIN_Y = -(WORLD_SIZE_Y / 2);
    public static final int MAX_Y = WORLD_SIZE_Y / 2 - 1;
    public static final int MAX_CAM_X = (MAX_X * 16) - 256;
    public static final int MIN_CAM_X = -MAX_CAM_X;

    public static void renderWorld() {
        renderAir();
        for (int y = 0; y < WORLD_SIZE_Y; y++) {
            for (int x = 0; x < WORLD_SIZE_X; x++) {
                WorldPos pos = WorldBuilder.WORLD_DATA[x][y];
                Tile tile = Registry.getTile(pos.tile);
                tile.renderTileAtPos((x - MAX_X - 0.5f) * 16f, (y - MAX_Y - 0.5f) * 16f);
            }
        }
    }

    private static void renderAir() {
        glfwMakeContextCurrent(DisplayManager.win);
        glBegin(GL_QUADS);
        Vector2f pos = Entities.PLAYER.getPosition();
        int x = (int) Math.max(pos.x, MIN_CAM_X);
        x = Math.min(x, MAX_CAM_X);
        glColor4f(37 / 255f, 23 / 255f, 99 / 255f, 1);
        glVertex2f(-256 + x, 128 + pos.y);
        glColor4f(37 / 255f, 23 / 255f, 99 / 255f, 1);
        glVertex2f(256 + x, 128 + pos.y);
        glColor4f(14 / 255f, 9 / 255f, 38 / 255f, 1);
        glVertex2f(256 + x, -128 + pos.y);
        glColor4f(14 / 255f, 9 / 255f, 38 / 255f, 1);
        glVertex2f(-256 + x, -128 + pos.y);
        glEnd();
        glColor4f(1, 1, 1, 1);
    }

    public static void setTileAtPos(String name, int x, int y) {
        WorldPos pos = WorldBuilder.WORLD_DATA[x + MAX_X + 1][y + MAX_Y + 1];
        pos.tile = name;
        WorldBuilder.WORLD_DATA[x + MAX_X + 1][y + MAX_Y + 1] = pos;
    }

    public static Tile getTileAtPosition(int x, int y) {
        int modX, modY;
        if (x < 0) {
            modX = MAX_X;
        } else {
            modX = MAX_X + 1;
        }
        modY = MAX_Y + 1;
        x = verifyX(x);
        y = verifyY(y);
        int fx = x + modX;
        if (fx < 0) fx = 0;
        WorldPos pos = WorldBuilder.WORLD_DATA[fx][y + modY];
        String tile = pos.tile;
        return Registry.getTile(tile);
    }

    public static int verifyX(int x) {
        x = Math.max(x, MIN_X);
        x = Math.min(x, MAX_X);
        return x;
    }

    public static int verifyY(int y) {
        y = Math.max(y, MIN_Y);
        y = Math.min(y, MAX_Y);
        return y;
    }

}
