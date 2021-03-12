package io.innocent.dream.worldBuilder;

import io.innocent.dream.InnocentDream;

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
        float result = WORLD_GENERATION_RANDOM.nextFloat() * 8;
        return result - 8;
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

    private static float getFinalHeight(int x) {
        float sides = (getSmoothHeight(x - 1) + getSmoothHeight(x + 1)) / 2f;
        float center = getSmoothHeight(x);
        return (sides + center) / 2f;
    }

    public static void generateWorld(int worldInt) {
        for (int y = WorldTileManager.MIN_Y; y < WorldTileManager.MAX_Y + 1; y++) {
            for (int x = WorldTileManager.MIN_X; x < WorldTileManager.MAX_X + 1; x++) {
                WorldPos pos = new WorldPos(x, y);
                WORLD_DATA[x + WorldTileManager.MAX_X + 1][y + WorldTileManager.MAX_Y + 1] = pos;
            }
        }

        for (int x = WorldTileManager.MIN_X; x < WorldTileManager.MAX_X + 1; x++) {
            int y = (int) getFinalHeight(x);
            WorldTileManager.setTileAtPos("grass", x, y);
            int i = 0;
            while (y > WorldTileManager.MIN_Y) {
                y -= 1;
                i += 1;
                if (i > 3) {
                    WorldTileManager.setTileAtPos("stone", x, y);
                } else {
                    WorldTileManager.setTileAtPos("dirt", x, y);
                }
            }
        }
        world.createNewWorld(new File(InnocentDream.path + "\\worlds\\World" + worldInt));
    }

}
