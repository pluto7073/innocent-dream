package io.innocent.dream.worldBuilder;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.entities.Entities;
import io.innocent.dream.inventory.*;
import io.innocent.dream.util.TickEventListener;
import io.innocent.dream.worldBuilder.biomes.*;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.joml.Vector2f;
import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class World {

    private static final Random WORLD_RANDOM = new Random();

    private final List<TickEventListener> tickEventListeners;
    private final HashMap<Vector2f, AbstractInventory> worldChestInventories;
    
    private long seed;
    private final int entityCap = 2;
    public boolean isSinglePlayer;
    private HashMap<String, Object> worldInformation;

    public World() {
        tickEventListeners = new ArrayList<>();
        worldChestInventories = new HashMap<>();
        this.isSinglePlayer = true;
        Biomes.init();
    }

    public void loadWorld(File worldFolder) {
    	try {
    		File f = new File(worldFolder.getAbsolutePath() + "\\worldInformation.id");
    		DataInputStream dis = new DataInputStream(new GZIPInputStream(new FileInputStream(f)));
    		byte[] worldDataBytes = new byte[(int) f.length()];
    		dis.readFully(worldDataBytes);
    		dis.close();
    		HashMap<String, Object> worldSettings = (HashMap<String, Object>) byteArrayToObject(worldDataBytes);
    		WorldBuilder.WORLD_SEED = (long) worldSettings.get("world-seed");
    	} catch (IOException e) {
    		InnocentDream.crashReportBuilder.createCrashReport(e);
    	}
    	
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
        worldInformation = new HashMap<>();
        File tempFolder = new File(InnocentDream.path + "\\temp\\world");
        tempFolder.mkdirs();
        worldFolder.mkdirs();
        saveWorld(worldFolder);
    }
    
    public void saveWorld(File worldFolder) {
        worldFolder.mkdirs();
        JSONObject worldObject = new JSONObject();
        worldObject.put("seed", seed);
        JSONArray tilesArray = WorldSaver.saveTiles();
        worldObject.put("tiles", tilesArray);
        worldInformation.put("world", worldObject);

        JSONArray entities = WorldSaver.saveEntities();
        JSONArray playerInv = WorldSaver.savePlayerInventory();
        JSONObject inventories = new JSONObject();
        inventories.put("player", playerInv);
        worldInformation.put("inventories", inventories);
        worldInformation.put("entities", entities);
        
        //World Information File (worldInformation.id)
        HashMap<String, Object> worldSettings = new HashMap<>();
        worldSettings.put("world-seed", seed);
        worldSettings.put("world-biomelist", "currently not a feature");
        try {
        	DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(worldFolder.getAbsolutePath() + "\\worldInformation.id")));
        	byte[] ba = objectToByteArray(worldSettings);
        	System.out.println(ba.length);
        	dos.write(ba);
        	dos.close();
        } catch(IOException e) {
        	e.printStackTrace();
        }
        
        //World Tiles (<world folder>\tiles)
        WorldPos[][] tiles = WorldBuilder.WORLD_DATA;
        (new File(worldFolder.getAbsolutePath() + "\\tiles")).mkdirs();
        for (int i = 0; i < tiles.length; i++) {
        	WorldPos[] row = tiles[i];
        	try {
        		DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(
        				new FileOutputStream(worldFolder.getAbsolutePath() + "\\tiles\\" + i + ".idr")));
        		dos.write(objectToByteArray(row));
        		dos.close();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
        
        (new File(worldFolder.getAbsolutePath() + "\\entitydata")).mkdirs();
        
        //Player Information
        HashMap<String, Object> playerInformation = new HashMap<>();
        playerInformation.put("coordinatex", Entities.PLAYER.getPosition().x);
        playerInformation.put("coordinatey", Entities.PLAYER.getPosition().y);
        playerInformation.put("hitpoints", Entities.PLAYER.getHp());
        playerInformation.put("inventory", Entities.PLAYER.inventory.toJSONArray());
        try {
        	DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(
        			new FileOutputStream(worldFolder.getAbsolutePath() + "\\entitydata\\" + Entities.PLAYER.getUID() + ".ide")));
        	dos.write(objectToByteArray(playerInformation));
        	dos.close();
        } catch (IOException e) {
        	e.printStackTrace();
        }
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
    
    private static Object byteArrayToObject(byte[] array) {
    	try {
    		ByteArrayInputStream in = new ByteArrayInputStream(array);
    		ObjectInputStream is = new ObjectInputStream(in);
    		return is.readObject();
    	} catch (IOException e) {
    		InnocentDream.crashReportBuilder.createCrashReport(e, "An IO Exception Occured When Trying To Read Byte Data");
    	} catch (ClassNotFoundException e) {
    		InnocentDream.crashReportBuilder.createCrashReport(e, "The byte data stored a class that was either Invalid, or no longer exists");
    	}
    	return new Object();
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
    
    public Biome getBiomeAtCoordinate(int x) {
    	return WorldBuilder.WORLD_DATA[x][0].biome;
    }

}
