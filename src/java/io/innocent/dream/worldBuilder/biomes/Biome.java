package io.innocent.dream.worldBuilder.biomes;

import io.innocent.dream.tile.Tile;

public class Biome {
	
	protected final int biomeID;
	protected final int naturalHeight;
	protected final Tile surfaceTile;
	protected final Tile underTile;
	protected final Tile baseStoneTile;

	public Biome(int biomeID, int naturalHeight, Tile surfaceTile, Tile underTile, Tile baseStoneTile) {
		this.biomeID = biomeID;
		this.naturalHeight = naturalHeight;
		this.surfaceTile = surfaceTile;
		this.underTile = underTile;
		this.baseStoneTile = baseStoneTile;
	}
	
	public int getNaturalHeight() {
		return naturalHeight;
	}
	
	public Tile getSurfaceTile() {
		return surfaceTile;
	}
	
	public Tile getUnderTile()  {
		return underTile;
	}
	
	public Tile getBaseStoneTile() {
		return baseStoneTile;
	}

}
