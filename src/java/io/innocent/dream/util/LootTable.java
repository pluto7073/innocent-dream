package io.innocent.dream.util;

import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.item.Item;
import io.innocent.dream.registry.Registry;
import io.innocent.dream.util.LootTable.NumberChanceGenerator.Type;

public class LootTable {
	
	private JSONObject lootTable;
	private int rolls;
	private LootTableItem[] lootTableItems;

	public LootTable(String tableFile) {
		try {
			InputStream lis = InnocentDream.getResAsStream(tableFile);
			StringBuilder tableBuilder = new StringBuilder();
			Scanner scanner = new Scanner(lis);
			while (scanner.hasNextLine()) {
				tableBuilder.append(scanner.nextLine()).append("\n");
			}
			scanner.close();
			JSONParser parser = new JSONParser();
			lootTable = (JSONObject) parser.parse(tableBuilder.toString());
		} catch (Exception e) {
			System.err.println("An Error Occurred when trying to load loot table: " + tableFile);
			InnocentDream.crashReportBuilder.createCrashReport(e);
			return;
		}
		loadTable();
	}
	
	private void loadTable() {
		JSONObject loot = (JSONObject) lootTable.get("loot");
		long rolls = (long) loot.get("rolls");
		this.rolls = (int) rolls;
		JSONArray items = (JSONArray) loot.get("items");
		int i = 0;
		for (Object o : items) {
			JSONObject itemData = (JSONObject) o;
			Item item = Registry.getItem((String) itemData.get("item"));
			double chance = (double) itemData.get("chance");
			JSONObject countObj = (JSONObject) itemData.get("count");
			LootTableItem lti = new LootTableItem(item, (float) chance, loadCounts(countObj));
			lootTableItems[i] = lti;
			i++;
		}
	}
	
	private NumberChanceGenerator loadCounts(JSONObject obj) {
		String t = (String) obj.get("type");
		Type type = Type.valueOf(t);
		JSONArray array = (JSONArray) obj.get("values");
		int[] values = new int[array.size()];
		for (int i = 0; i < array.size(); i++) {
			long l = (long) array.get(i);
			values[i] = (int) l;
		}
		return new NumberChanceGenerator(type, values);
	}
	
	public int getRolls() {
		return rolls;
	}
	
	public LootTableItem[] getTableItems() {
		return lootTableItems;
	}
	
	public static class NumberChanceGenerator {
		
		private Random random = new Random();
		
		public enum Type {
			INT,
			CHANCE,
			RANGE
		}
		
		private Type type;
		private int[] values;
		
		public NumberChanceGenerator(Type type, int[] values) {
			this.type = type;
			this.values = values;
		}
		
		public int getNumber() {
			int number = 0;
			if (type.equals(Type.INT)) {
				number = values[0];
			} else if (type.equals(Type.CHANCE)) {
				int index = random.nextInt(values.length);
				number = values[index];
			} else if (type.equals(Type.RANGE)) {
				int min = values[0];
				int max = values[1];
				int bound  = max - min;
				number = random.nextInt(bound) + min;
			}
			return number;
		}
		
	}
	
	public static class LootTableItem {
		
		private Item item;
		private float chance;
		private NumberChanceGenerator count;
		
		public LootTableItem(Item item, float chance, NumberChanceGenerator count) {
			this.item = item;
			this.chance = chance;
			this.count = count;
		}
		
		public Item getItem() {
			return item;
		}
		
		public float getChance() {
			return chance;
		}
		
		public int getCount() {
			return count.getNumber();
		}
		
	}

}
