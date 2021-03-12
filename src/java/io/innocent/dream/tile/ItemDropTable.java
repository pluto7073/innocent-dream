package io.innocent.dream.tile;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.item.Item;
import io.innocent.dream.item.Items;
import io.innocent.dream.registry.Registry;
import org.joml.Vector2f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ItemDropTable {

    private static class DropData {
        private final Item item;
        private final int count;
        public DropData(Item item, int count) {
            this.item = item;
            this.count = count;
        }
        public Item getItem() {
            return item;
        }
        public int getCount() {
            return count;
        }
    }

    private final List<DropData> dropData;

    public ItemDropTable(String path) {
        dropData = new ArrayList<>();
        try {
            InputStream is = InnocentDream.getResAsStream(path);
            StringBuilder tableBuilder = new StringBuilder();
            Scanner scanner = new Scanner(is);
            while (scanner.hasNextLine()) {
                tableBuilder.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
            JSONObject object = (JSONObject) new JSONParser().parse(tableBuilder.toString());
            JSONArray itemsToDrop = (JSONArray) object.get("items");
            for (Object o : itemsToDrop) {
                JSONObject obj = (JSONObject) o;
                String itemID = (String) obj.get("item");
                int count = Integer.parseInt(String.valueOf(obj.get("count")));
                Item item = Registry.getItem(itemID);
                DropData data = new DropData(item, count);
                dropData.add(data);
            }
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void spawnItems(Vector2f position) {
        for (DropData data : dropData) {
            Item item = data.getItem();
            int count = data.getCount();
            Items.createItemEntity(item, count, position);
        }
    }

}
