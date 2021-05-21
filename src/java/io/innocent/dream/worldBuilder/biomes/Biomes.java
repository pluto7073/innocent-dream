package io.innocent.dream.worldBuilder.biomes;

import io.innocent.dream.registry.Registry;
import io.innocent.dream.tile.Tiles;

public final class Biomes {
	
	public static final Biome DEFAULT;
	public static final Biome MOUNTAINS;
	
	static {
		DEFAULT = Registry.register(0, new Biome(0, 0, Tiles.GRASS, Tiles.DIRT, Tiles.STONE));
		MOUNTAINS = Registry.register(1, new Biome(1, 20, Tiles.STONE, Tiles.STONE, Tiles.STONE));
	}
	
	public static final void init() {}
	
}
