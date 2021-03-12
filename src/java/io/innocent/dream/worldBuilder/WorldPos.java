package io.innocent.dream.worldBuilder;

public class WorldPos {

    public float x;
    public float y;
    public String tile;

    public WorldPos(float x, float y) {
        this(x, y, "air");
    }

    public WorldPos(float x, float y, String tile) {
        this.x = x;
        this.y = y;
        this.tile = tile;
    }

}
