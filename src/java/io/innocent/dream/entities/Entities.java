package io.innocent.dream.entities;

import io.innocent.dream.entities.enemies.FlyingSlime;
import io.innocent.dream.registry.Registry;
import java.util.HashMap;

public class Entities {

    public static final Player PLAYER;
    public static final FlyingSlime FLYING_SLIME;

    static {
        PLAYER = Registry.register("player", new Player());
        FLYING_SLIME = Registry.register("flying_slime", new FlyingSlime());
    }

    public static void init() {

    }

    public static void renderEntities() {
        HashMap<String, Entity> entities = Registry.getEntitiesRegistry();
        entities.forEach((s, entity) -> {
            entity.tick();
            entity.render();
        });
    }

}
