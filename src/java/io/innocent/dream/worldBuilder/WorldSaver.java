package io.innocent.dream.worldBuilder;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.entities.Entities;
import io.innocent.dream.entities.Entity;
import io.innocent.dream.entities.EntityWithHP;
import io.innocent.dream.entities.Player;
import io.innocent.dream.entities.enemies.FlyingSlime;
import io.innocent.dream.inventory.Slot;
import io.innocent.dream.item.Item;
import io.innocent.dream.registry.Registry;
import org.joml.Vector2f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;

public class WorldSaver {

    public static final JSONParser PARSER = new JSONParser();

    public static File worldPath;

    public static void loadWorld(File saveFile) {
        worldPath = saveFile;
        WorldBuilder.world.loadWorld(saveFile);
        try {
            JSONObject world = WorldBuilder.world.getTilesInWorld();
            JSONArray tiles = (JSONArray) world.get("tiles");
            int MAX_X = WorldTileManager.WORLD_SIZE_X;
            int MAX_Y = WorldTileManager.WORLD_SIZE_Y;
            WorldPos[][] WORLD = new WorldPos[MAX_X][MAX_Y];
            for (int y = 0; y < MAX_Y; y++) {
                JSONArray Y = (JSONArray) tiles.get(y);
                for (int x = 0; x < MAX_X; x++) {
                    JSONObject X = (JSONObject) Y.get(x);
                    WorldPos pos = new WorldPos(x - (MAX_X / 2f), y - (MAX_Y / 2f));
                    pos.tile = (String) X.get("tileName");
                    WORLD[x][y] = pos;
                }
            }
            WorldBuilder.WORLD_DATA = WORLD;
            JSONObject inventories = WorldBuilder.world.getInventories();
            JSONArray entities = WorldBuilder.world.getEntitiesInWorld();
            for (Object o : entities) {
                JSONObject entityObj = (JSONObject) o;
                String type = (String) entityObj.get("type");
                if ("player".equals(type)) {
                    double location_x = (double) entityObj.get("location_x");
                    double location_y = (double) entityObj.get("location_y");
                    double hp = (double) entityObj.get("hp");
                    String inventoryPointer = (String) entityObj.get("inventory");
                    JSONArray inventory = (JSONArray) inventories.get(inventoryPointer);
                    Player player = Entities.PLAYER;
                    player.setHp(Float.parseFloat(String.valueOf(hp)));
                    Vector2f pos = new Vector2f(Float.parseFloat(String.valueOf(location_x)),
                            Float.parseFloat(String.valueOf(location_y)));
                    player.getPosition().x = pos.x;
                    player.getPosition().y = pos.y;
                    loadPlayerInventory(inventory);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadPlayerInventory(JSONArray inventory) {
        int i = 0;
        for (Object o : inventory) {
            JSONObject itemObj = (JSONObject) o;
            Item item = Registry.getItem((String) itemObj.get("item"));
            int count = Integer.parseInt(String.valueOf(itemObj.get("count")));
            Entities.PLAYER.inventory.setItemWithCountAtIndex(item, count, i);
            i++;
        }
    }

    public static void saveWorld() {
        WorldBuilder.world.saveWorld(worldPath);
    }

	public static JSONArray saveEntities() {
        JSONArray entitiesObject = new JSONArray();
        HashMap<String, Entity> entitiesMap = Registry.getEntitiesRegistry();
        entitiesMap.forEach((id, entity) -> {
            JSONObject entityObj = new JSONObject();
            System.out.println("Saving Entity: " + id);
            entityObj.put("type", id);
            entityObj.put("location_x", entity.getPosition().x);
            entityObj.put("location_y", entity.getPosition().y);
            if (entity instanceof EntityWithHP) {
                entityObj.put("hp", ((EntityWithHP) entity).getHp());
            }
            if (entity instanceof FlyingSlime) {
                entityObj.put("instances", saveSlimes((FlyingSlime) entity));
            }

            if (entity instanceof Player) {
                entityObj.put("inventory", "player");
            }
            entitiesObject.add(entityObj);
        });
        return entitiesObject;
    }

    public static JSONArray savePlayerInventory() {
        JSONArray inventory = new JSONArray();
        for (Slot s : Entities.PLAYER.inventory) {
            Item i = s.getItem();
            int count = s.getCount();
            JSONObject item = new JSONObject();
            item.put("item", i.getName());
            item.put("count", count);
            inventory.add(item);
        }
        return inventory;
    }

    private static JSONArray saveSlimes(FlyingSlime topLevel) {
        JSONArray array = new JSONArray();
        for (FlyingSlime slime : topLevel.getInstances()) {
            JSONObject obj = new JSONObject();
            obj.put("location_x", slime.getPosition().x);
            obj.put("location_y", slime.getPosition().y);
            obj.put("hp", slime.getHp());
            array.add(obj);
        }
        return array;
    }

    public static JSONArray saveTiles() {
        WorldPos[][] WORLD = WorldBuilder.WORLD_DATA;
        JSONArray tiles = new JSONArray();
        for (int y = 0; y < WorldTileManager.WORLD_SIZE_Y; y++) {
            JSONArray Y = new JSONArray();
            for (int x = 0; x < WorldTileManager.WORLD_SIZE_X; x++) {
                JSONObject X = new JSONObject();
                WorldPos pos = WORLD[x][y];
                X.put("tileName", pos.tile);
                Y.add(X);
            }
            tiles.add(Y);
        }
        try {
			DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(new File(InnocentDream.path + "\\worldSaveTest.id"))));
			dos.write(objectToByteArray(WORLD));
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return tiles;
    }
    
    private static byte[] objectToByteArray(Object object) {
    	try {
    		ByteArrayOutputStream out = new ByteArrayOutputStream();
    		ObjectOutputStream os = new ObjectOutputStream(out);
    		os.writeObject(object);
    		return out.toByteArray();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return null;
    }

}
