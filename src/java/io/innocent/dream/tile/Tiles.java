package io.innocent.dream.tile;

import io.innocent.dream.drawing.Texture;
import io.innocent.dream.registry.Registry;

public class Tiles {

    public static final Tile GRASS;
    public static final Tile DIRT;
    public static final Tile STONE;
    public static final Tile AIR;
    public static final Tile CAVE_AIR;
    public static final Tile DIRT_WALL;
    public static final Tile CHEST;

    static {
        GRASS = Registry.register("grass", new Tile(Tile.Type.BLOCK, new Texture("assets/tiles/tile-map.png", 0, 0, 32, 32), "data/tiles/grass.iddt"));
        DIRT = Registry.register("dirt", new Tile(Tile.Type.BLOCK, new Texture("assets/tiles/tile-map.png", 32, 0, 32, 32), "data/tiles/dirt.iddt"));
        STONE = Registry.register("stone", new Tile(Tile.Type.BLOCK, new Texture("assets/tiles/tile-map.png", 64, 0, 32, 32), "data/tiles/stone.iddt"));
        AIR = Registry.register("air", new AirTile());
        DIRT_WALL = Registry.register("dirt_wall", new Tile(Tile.Type.AIR, new Texture("assets/tiles/tile-map.png", 0, 32, 32, 32)));
        CAVE_AIR = Registry.register("cave_air", new Tile(Tile.Type.AIR, new Texture("assets/tiles/tile-map.png", 32, 32, 32, 32)));
        CHEST = Registry.register("chest", new ChestTile());
    }

    public static void init(){}

}
