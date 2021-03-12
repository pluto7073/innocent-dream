package io.innocent.dream.worldBuilder;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.inventory.AbstractInventory;
import io.innocent.dream.inventory.ChestInventory;
import io.innocent.dream.util.TickEventListener;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.joml.Vector2f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class World {

    private static final Random WORLD_RANDOM = new Random();

    private final List<TickEventListener> tickEventListeners;
    private final HashMap<Vector2f, AbstractInventory> worldChestInventories;
    
    private long seed;
    private final int entityCap = 2;
    public boolean isSinglePlayer;
    private JSONObject worldInformation;

    public World() {
        tickEventListeners = new ArrayList<>();
        worldChestInventories = new HashMap<>();
        this.isSinglePlayer = true;
    }

    public void loadWorld(File worldFolder) {
        worldInformation = new JSONObject();
        File tempFolder = new File(InnocentDream.path + "\\temp\\world");
        tempFolder.mkdirs();
        ZipFile world = new ZipFile(worldFolder.getAbsolutePath() + "\\world.id");
        ZipFile inventories = new ZipFile(worldFolder.getAbsolutePath() + "\\inventories.id");
        ZipFile entities = new ZipFile(worldFolder.getAbsolutePath() + "\\entities.id");
        try {
            world.extractFile("world", tempFolder.getAbsolutePath());
            inventories.extractFile("inventories", tempFolder.getAbsolutePath());
            entities.extractFile("entities", tempFolder.getAbsolutePath());
        } catch (ZipException e) {
            e.printStackTrace();
        }
        File worldFile = new File(InnocentDream.path + "\\temp\\world\\world");
        File inventoriesFile = new File(InnocentDream.path + "\\temp\\world\\inventories");
        File entitiesFile = new File(InnocentDream.path + "\\temp\\world\\entities");
        try (FileReader worldReader = new FileReader(worldFile);
            FileReader inventoriesReader = new FileReader(inventoriesFile);
            FileReader entitiesReader = new FileReader(entitiesFile)) {
            JSONParser parser = new JSONParser();
            JSONObject worldObject = (JSONObject) parser.parse(worldReader);
            JSONObject inventoriesObject = (JSONObject) parser.parse(inventoriesReader);
            JSONArray entitiesObject = (JSONArray) parser.parse(entitiesReader);
            this.seed = (long) worldObject.get("seed");
            WorldBuilder.WORLD_SEED = this.seed;
            worldInformation.put("world", worldObject);
            worldInformation.put("inventories", inventoriesObject);
            worldInformation.put("entities", entitiesObject);
            worldReader.close();
            inventoriesReader.close();
            entitiesReader.close();
            System.out.println("Successfully Read World Files");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        worldFile.delete();
        inventoriesFile.delete();
        entitiesFile.delete();
        tempFolder.delete();
    }

    public void createNewWorld(File worldFolder) {
        worldInformation = new JSONObject();
        File tempFolder = new File(InnocentDream.path + "\\temp\\world");
        tempFolder.mkdirs();
        worldFolder.mkdirs();
        saveWorld(worldFolder);
    }

	public void preSave() {
        //World & Tiles
        JSONObject worldObject = new JSONObject();
        worldObject.put("seed", seed);
        JSONArray tilesArray = WorldSaver.saveTiles();
        worldObject.put("tiles", tilesArray);
        worldInformation.put("world", worldObject);

        //Everything Else
        JSONArray entities = WorldSaver.saveEntities();
        JSONArray playerInv = WorldSaver.savePlayerInventory();
        JSONObject inventories = new JSONObject();
        inventories.put("player", playerInv);
        worldInformation.put("inventories", inventories);
        worldInformation.put("entities", entities);
    }

    public void saveWorld(File worldFolder) {
        worldFolder.mkdirs();
        preSave();
        File tempFolder = new File(InnocentDream.path + "\\temp\\world");
        tempFolder.mkdirs();
        File worldFile = new File(InnocentDream.path + "\\temp\\world\\world");
        File inventoriesFile = new File(InnocentDream.path + "\\temp\\world\\inventories");
        File entitiesFile = new File(InnocentDream.path + "\\temp\\world\\entities");
        try {
            worldFile.createNewFile();
            inventoriesFile.createNewFile();
            entitiesFile.createNewFile();
            JSONObject world = (JSONObject) worldInformation.get("world");
            JSONObject inventories = (JSONObject) worldInformation.get("inventories");
            JSONArray entities = (JSONArray) worldInformation.get("entities");
            FileWriter masterWriter = new FileWriter(worldFile);
            world.writeJSONString(masterWriter);
            masterWriter.close();
            masterWriter = new FileWriter(inventoriesFile);
            inventories.writeJSONString(masterWriter);
            masterWriter.close();
            masterWriter = new FileWriter(entitiesFile);
            entities.writeJSONString(masterWriter);
            masterWriter.close();
            System.out.println("Successfully saved world");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(worldFolder.getAbsolutePath() + "\\.version")) {
            writer.write(InnocentDream.version);
            writer.close();
            System.out.println("Updated the current saved Version");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String worldPath = worldFolder.getAbsolutePath();
        ZipFile worldID = new ZipFile(worldPath + "\\world.id");
        ZipFile inventoriesID = new ZipFile(worldPath + "\\inventories.id");
        ZipFile entitiesID = new ZipFile(worldPath + "\\entities.id");
        try {
            worldID.addFile(worldFile);
            inventoriesID.addFile(inventoriesFile);
            entitiesID.addFile(entitiesFile);
        } catch (ZipException e) {
            e.printStackTrace();
        }
        worldFile.delete();
        inventoriesFile.delete();
        entitiesFile.delete();
        tempFolder.delete();

    }

    public void addTickEventListener(TickEventListener l) {
        tickEventListeners.add(l);
    }

    public void tick() {
        int randomTick = WORLD_RANDOM.nextInt(10);
        if (randomTick == 4) {
            for (TickEventListener l : tickEventListeners) {
                l.onRandomTick(this);
            }
        }
    }
    
    public AbstractInventory loadChestInventory(Vector2f pos) {
    	AbstractInventory inventory = worldChestInventories.get(pos);
    	if (inventory != null) {
    		return inventory;
    	}
    	return new ChestInventory();
    }
    
    public void saveChestInventory(Vector2f pos, AbstractInventory inventory) {
    	worldChestInventories.put(pos, inventory);
    }

    public int getEntityCap() {
        return entityCap;
    }

    public JSONObject getTilesInWorld() {
        return (JSONObject) worldInformation.get("world");
    }

    public JSONArray getEntitiesInWorld() {
        return (JSONArray) worldInformation.get("entities");
    }

    public JSONObject getInventories() {
        return (JSONObject) worldInformation.get("inventories");
    }

}
