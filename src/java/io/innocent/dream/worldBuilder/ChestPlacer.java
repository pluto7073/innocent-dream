package io.innocent.dream.worldBuilder;

import java.util.Random;

import org.joml.Vector2f;

import io.innocent.dream.inventory.ChestInventory;
import io.innocent.dream.tile.Tile;

public class ChestPlacer {
	
	public static class ChestInvHolder {
		private Vector2f position;
		private ChestInventory inventory;
		public ChestInvHolder(Vector2f position, ChestInventory inventory) {
			this.position = position;
			this.inventory = inventory;
		}
		public Vector2f getPosition() {
			return position;
		}
		public ChestInventory getInventory() {
			return inventory;
		}
	}
	
	public static final Random CHEST_RANDOM = new Random();

	private ChestPlacer() {}
	
	public static ChestInvHolder[] generateChestLoot() {
		ChestInvHolder[] invs = new ChestInvHolder[3];
		
		return invs;
	}
	
	public static Vector2f[] generateChestPositions() {
		Vector2f[] chests = new Vector2f[3];
		for (int i = 0; i < chests.length; i++) {
			boolean touchingAir = true;
			Vector2f position = new Vector2f();
			while (touchingAir) {
				int sizeX = WorldTileManager.MAX_X;
				int sizeY = WorldTileManager.MAX_Y;
				int x = CHEST_RANDOM.nextInt(WorldTileManager.WORLD_SIZE_X);
				int y = CHEST_RANDOM.nextInt(WorldTileManager.WORLD_SIZE_Y);
				x -= sizeX;
				y -= sizeY;
				touchingAir = getTouchingAir(x, y);
			}
			chests[i] = position;
		}
		return chests;
	}
	
	private static boolean getTouchingAir(int x, int y) {
		Tile right = WorldTileManager.getTileAtPosition(x + 1, y);
		Tile left = WorldTileManager.getTileAtPosition(x - 1, y);
		Tile top = WorldTileManager.getTileAtPosition(x, y + 1);
		Tile bottom = WorldTileManager.getTileAtPosition(x, y - 1);
		if (!top.getType().equals(Tile.Type.BLOCK)) return true;
		if (!bottom.getType().equals(Tile.Type.BLOCK)) return true;
		if (!right.getType().equals(Tile.Type.BLOCK)) return true;
		if (!left.getType().equals(Tile.Type.BLOCK)) return true;
		return false;
	}

}
