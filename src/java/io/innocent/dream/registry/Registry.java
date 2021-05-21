package io.innocent.dream.registry;

import io.innocent.dream.actions.Action;
import io.innocent.dream.entities.Entity;
import io.innocent.dream.exceptions.ActionNotFoundException;
import io.innocent.dream.exceptions.ItemNotFoundException;
import io.innocent.dream.item.Item;
import io.innocent.dream.tile.Tile;
import io.innocent.dream.worldBuilder.biomes.Biome;
import io.innocent.dream.exceptions.TileNotFoundException;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class Registry {

    private static final HashMap<String, Tile> tileRegistry = new HashMap<>();
    private static final HashMap<String, Action> actionsRegistry = new HashMap<>();
    private static final HashMap<String, Entity> entitiesRegistry = new HashMap<>();
    private static final HashMap<String, Item> itemsRegistry = new HashMap<>();
    private static final HashMap<Integer, Biome> biomeRegistry = new HashMap<>();

    private Registry(){}

    public static <T, V extends T> V register(Object id, V item) {
        if (item instanceof Tile) {
            tileRegistry.put((String) id, (Tile) item);
        } else if (item instanceof Action) {
            actionsRegistry.put((String) id, (Action) item);
        } else if (item instanceof Entity) {
            entitiesRegistry.put((String) id, (Entity) item);
        } else if (item instanceof Item) {
            itemsRegistry.put((String) id, (Item) item);
        } else if (item instanceof Biome) {
        	biomeRegistry.put((Integer) id, (Biome) item);
        } else {
            System.out.println("Could not find the corresponding registry for " + id);
        }
        return item;
    }

    public static Tile getTile(String id) {
        Tile tile = tileRegistry.get(id);
        if (tile == null) {
            try {
                throw new TileNotFoundException(id);
            } catch (TileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return tile;
    }

    public static String getTileName(Tile tile) {
        AtomicReference<String> name = new AtomicReference<>("air");
        tileRegistry.forEach((s, t) -> {
            if (tile == t) {
                name.set(s);
            }
        });
        return name.get();
    }

    public static Action getAction(String id) {
        Action action = actionsRegistry.get(id);
        if (action == null) {
            try {
                throw new ActionNotFoundException(id);
            } catch (ActionNotFoundException e) {
                e.printStackTrace();
            }
        }
        return action;
    }

    public static Item getItem(String id) {
        Item item = itemsRegistry.get(id);
        if (item == null) {
            try {
                throw new ItemNotFoundException(id);
            } catch (ItemNotFoundException e) {
                e.printStackTrace();
            }
        }
        return item;
    }

    public static HashMap<String, Action> getActionsRegistry() {
        return actionsRegistry;
    }

    public static HashMap<String, Entity> getEntitiesRegistry() {
        return entitiesRegistry;
    }
    
    public static Biome getBiomeFromID(int biomeID) {
    	return biomeRegistry.get(biomeID);
    }
    
   
}
