package io.innocent.dream.registry;

import io.innocent.dream.actions.AbstractAction;
import io.innocent.dream.exceptions.ActionNotFoundException;
import io.innocent.dream.tile.Tile;
import io.innocent.dream.exceptions.TileNotFoundException;

import java.util.HashMap;

public class Registry {

    private static final HashMap<String, Tile> tileRegistry = new HashMap<>();
    private static final HashMap<String, AbstractAction> actionsRegistry = new HashMap<>();

    private Registry(){}

    public static <T, V extends T> V register(String id, V item) {
        if (item instanceof Tile) {
            tileRegistry.put(id, (Tile) item);
        } else if (item instanceof AbstractAction) {
            actionsRegistry.put(id, (AbstractAction) item);
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

    public static AbstractAction getAction(String id) {
        AbstractAction action = actionsRegistry.get(id);
        if (action == null) {
            try {
                throw new ActionNotFoundException(id);
            } catch (ActionNotFoundException e) {
                e.printStackTrace();
            }
        }
        return action;
    }

    public static HashMap<String, AbstractAction> getActionsRegistry() {
        return actionsRegistry;
    }
}
