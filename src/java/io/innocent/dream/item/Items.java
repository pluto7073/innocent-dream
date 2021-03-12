package io.innocent.dream.item;

import io.innocent.dream.registry.Registry;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Items {

    public static final List<ItemEntity> RENDERED_ITEMS;
    public static final List<ItemEntity> ITEMS_TO_REMOVE;

    public static final Item AIR_ITEM;
    public static final Item DIRT;
    public static final Item GRASS;
    public static final Item STONE;

    static {
        RENDERED_ITEMS = new ArrayList<>();
        ITEMS_TO_REMOVE = new ArrayList<>();
        AIR_ITEM = Registry.register("air", new Item("data/items", "air"));
        DIRT = Registry.register("dirt", new Item("data/items", "dirt"));
        GRASS = Registry.register("grass", new Item("data/items", "grass"));
        STONE = Registry.register("stone", new Item("data/items", "stone"));
        Registry.register("cave_air", STONE);
        Registry.register("dirt_wall", DIRT);
    }

    public static void createItemEntity(Item item, int count, Vector2f pos) {
        ItemEntity itemEntity = new ItemEntity(item.getName(), count, pos);
        RENDERED_ITEMS.add(itemEntity);
    }

    public static void renderRenderedItems() {
        ITEMS_TO_REMOVE.clear();
        for (ItemEntity i : RENDERED_ITEMS) {
            i.render();
            i.tick();
        }
        for (ItemEntity i : ITEMS_TO_REMOVE) {
            RENDERED_ITEMS.remove(i);
        }
    }

}
