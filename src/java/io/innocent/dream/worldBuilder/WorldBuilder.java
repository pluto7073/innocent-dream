package io.innocent.dream.worldBuilder;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.registry.Registry;
import io.innocent.dream.worldBuilder.biomes.Biome;
import io.innocent.dream.worldBuilder.biomes.Biomes;

import java.io.File;
import java.util.Random;

public class WorldBuilder {

    public static Random WORLD_GENERATION_RANDOM;
    public static long WORLD_SEED;
    public static final float GRAVITY = -0.1f;
    static WorldPos[][] WORLD_DATA;
    public static boolean loadedFromFile;
    public static World world;
    
    static {
    	world = new World();
    }

    public static void init(int worldInt) {
    	new File(InnocentDream.path + "\\worlds").mkdirs();
        File file = new File(InnocentDream.path + "\\worlds\\World" + worldInt);
        if (!file.exists()) {
            createWorld();
        } else {
            loadedFromFile = true;
            world = new World();
            WorldSaver.loadWorld(file);
        }
    }

    public static void createWorld() {
        loadedFromFile = false;
        WORLD_GENERATION_RANDOM = new Random();
        WORLD_SEED = WORLD_GENERATION_RANDOM.nextInt();
        System.out.println("World Seed: " + WORLD_SEED);
        WORLD_DATA = new WorldPos[WorldTileManager.WORLD_SIZE_X][WorldTileManager.WORLD_SIZE_Y];
        world = new World();
    }

    private static float getHeight(int x) {
        WORLD_GENERATION_RANDOM.setSeed(x * 23489 + WORLD_SEED);
        float result = WORLD_GENERATION_RANDOM.nextFloat() * 16;
        return (result - 8) + world.getBiomeAtCoordinate(x).getNaturalHeight();
    }

    private static float getAverageHeight(int x) {
        float sides = (getHeight(x - 1) + getHeight(x + 1)) / 2f;
        float center = getHeight(x);
        return (sides + center) / 2f;
    }

    private static float getSmoothHeight(int x) {
        float sides = (getAverageHeight(x - 1) + getAverageHeight(x + 1)) / 2f;
        float center = getAverageHeight(x);
        return (sides + center) / 2f;
    }
    
    private static float generateInterpolatedNoise(int x) {
    	int intX = (int) x;
    	float fracX = x - intX;
    	float v1 = getSmoothHeight(intX);
    	float v2 = getSmoothHeight(intX + 1);
    	float v3 = getSmoothHeight(intX - 1);
    	float i1 = interpolate(v1, v2, fracX);
    	float i2 = interpolate(v3, v1, fracX);
    	return interpolate(i1, i2, fracX);
    }
    
    private static float interpolate(float a, float b, float blend) {
    	double theta = blend * Math.PI;
    	float f = (float) (1f - Math.cos(theta)) * 0.5f;
    	return a * (1f - f) + b * f;
    }
    
    private static void generateBiomeMap() {
    	boolean canGenerateMountains = true;
    	for (int x = WorldTileManager.MIN_X; x < WorldTileManager.MAX_X + 1; x++) {
    		try {
    			if (world.getBiomeAtCoordinate(x) == Biomes.MOUNTAINS) continue;
    		} catch (NullPointerException ignored) {}
    		WORLD_GENERATION_RANDOM.setSeed(x * 38273 + WORLD_SEED);
    		int chance = WORLD_GENERATION_RANDOM.nextInt() % 50;
    		if ((chance == 1) && canGenerateMountains) {
    			canGenerateMountains = false;
    			for (int i = -5; i < 5; i++) {
    				WorldTileManager.setBiomeAtPosition(Biomes.MOUNTAINS, x + i);
    			}
    		} else {
    			WorldTileManager.setBiomeAtPosition(Biomes.DEFAULT, x);
    		}
    	}
    }
    
    private static void generateHeightMap() {
    	for (int x = WorldTileManager.MIN_X; x < WorldTileManager.MAX_X + 1; x++) {
            int y = (int) generateInterpolatedNoise(x);
            Biome biome = world.getBiomeAtCoordinate(x);
            WorldTileManager.setTileAtPos(Registry.getTileName(biome.getSurfaceTile()), x, y);
            int i = 0;
            while (y > WorldTileManager.MIN_Y) {
                y -= 1;
                i += 1;
                if (i > 3) {
                    WorldTileManager.setTileAtPos(Registry.getTileName(biome.getBaseStoneTile()), x, y);
                } else {
                    WorldTileManager.setTileAtPos(Registry.getTileName(biome.getUnderTile()), x, y);
                }
            }
        }
    }

    public static void generateWorld(int worldInt) {
        for (int y = WorldTileManager.MIN_Y; y < WorldTileManager.MAX_Y + 1; y++) {
            for (int x = WorldTileManager.MIN_X; x < WorldTileManager.MAX_X + 1; x++) {
                WorldPos pos = new WorldPos(x, y);
                WORLD_DATA[x + WorldTileManager.MAX_X + 1][y + WorldTileManager.MAX_Y + 1] = pos;
            }
        }
        generateBiomeMap();
        generateHeightMap();
        world.createNewWorld(new File(InnocentDream.path + "\\worlds\\World" + worldInt));
    }

}
