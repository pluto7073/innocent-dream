package io.innocent.dream.worldBuilder;

import java.io.Serializable;

import io.innocent.dream.worldBuilder.biomes.Biome;

public class WorldPos implements Serializable {

	private static final long serialVersionUID = 8518263552264657060L;
	public float x;
    public float y;
    public String tile;
    protected Biome biome;

    public WorldPos(float x, float y) {
        this(x, y, "air");
    }

    public WorldPos(float x, float y, String tile) {
        this.x = x;
        this.y = y;
        this.tile = tile;
    }
    
    public void setBiome(Biome b) {
    	this.biome = b;
    }
    
    public Biome getBiome() {
    	return biome;
    }

}
