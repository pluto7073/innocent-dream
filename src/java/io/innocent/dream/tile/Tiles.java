package io.innocent.dream.tile;

import io.innocent.dream.drawing.Texture;
import io.innocent.dream.registry.Registry;

public class Tiles {

    public static final Tile GRASS;
    public static final Tile DIRT;
    public static final Tile STONE;
    public static final Tile AIR;

    static {
        GRASS = Registry.register("grass", new Tile(Tile.Type.BLOCK, new Texture("assets/tiles/grass.png")));
        DIRT = Registry.register("dirt", new Tile(Tile.Type.BLOCK, new Texture("assets/tiles/grass.png")));
        STONE = Registry.register("stone", new Tile(Tile.Type.BLOCK, new Texture("assets/tiles/stone.png")));
        AIR = Registry.register("air", new Tile(Tile.Type.AIR, new Texture("assets/tiles/air.png")));
    }

    public static void init(){}

}
