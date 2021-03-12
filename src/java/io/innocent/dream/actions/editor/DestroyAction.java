package io.innocent.dream.actions.editor;

import io.innocent.dream.actions.Action;
import io.innocent.dream.entities.Entities;
import io.innocent.dream.tile.Tile;
import io.innocent.dream.tile.Tiles;
import io.innocent.dream.util.DisplayManager;
import io.innocent.dream.worldBuilder.WorldTileManager;
import org.joml.Vector2f;

public class DestroyAction extends Action {

    boolean ready = false;

    public DestroyAction() {
        super("key_destroy_block");
    }

    @Override
    public void preformAction() {
        ready = true;
    }

    @Override
    public void afterActionPreformed() {
        if (ready) {
            float[] pos = DisplayManager.getMousePositionRelativeToScreen();
            float xv = Entities.PLAYER.getPosition().x;
            xv = Math.max(xv, -768);
            xv = Math.min(xv, 768);
            pos[0] += xv;
            pos[0] /= 16;
            pos[1] += Entities.PLAYER.getPosition().y;
            pos[1] /= 16;
            pos[1] -= 1;
            int x = (int) (pos[0]);
            int y = (int) (pos[1]);
            Tile.Type type = WorldTileManager.getTileAtPosition(x, y).getType();
            ready = false;
            if (x < 0) x -= 1;
            if (type.equals(Tile.Type.BLOCK)) {
                Tile t = WorldTileManager.getTileAtPosition(x, y);
                t.onDestroy(new Vector2f(x * 16, y * 16));
                WorldTileManager.setTileAtPos("air", x, y);
                if (t.equals(Tiles.DIRT) || t.equals(Tiles.GRASS)) {
                	WorldTileManager.setTileAtPos("dirt_wall", x, y);
                } else if (t.equals(Tiles.STONE)) {
                	WorldTileManager.setTileAtPos("cave_air", x, y);
                }
            }
        }
        ready = false;
    }

}
