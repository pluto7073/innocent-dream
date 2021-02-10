package io.innocent.dream.worldBuilder;

import java.util.Random;

public class WorldBuilder {

    private static final Random WORLD_GENERATION_RANDOM;
    private static final long WORLD_SEED;
    static final WorldPos[][] WORLD_DATA;

    static {
        WORLD_GENERATION_RANDOM = new Random();
        WORLD_SEED = WORLD_GENERATION_RANDOM.nextInt();
        System.out.println("World Seed: " + WORLD_SEED);
        WORLD_DATA = new WorldPos[32][16];
        System.out.println(getHeight(10));
        System.out.println(getHeight(10));
        System.out.println(getHeight(45));
    }

    private static float getHeight(int x) {
        WORLD_GENERATION_RANDOM.setSeed(x * 23489 + WORLD_SEED);
        return WORLD_GENERATION_RANDOM.nextFloat() * 2f - 1f;
    }

    public static void generateWorld() {
        for (int y = -8; y < 8; y++) {
            for (int x = -16; x < 16; x++) {
                WorldPos pos = new WorldPos(x, y);
                WORLD_DATA[x + 16][y + 8] = pos;
            }
        }
    }

}
