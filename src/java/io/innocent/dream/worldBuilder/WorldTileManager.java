package io.innocent.dream.worldBuilder;

import io.innocent.dream.registry.Registry;
import io.innocent.dream.tile.Tile;

public class WorldTileManager {

    public static void renderWorld() {
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 32; x++) {
                
            }
        }
    }

    public static void setTileAtPos(String name, int x, int y) {
        Tile tile = Registry.getTile(name);
        tile.renderTileAtPos(x, y);
    }

}
