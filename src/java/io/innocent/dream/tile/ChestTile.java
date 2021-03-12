package io.innocent.dream.tile;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import io.innocent.dream.drawing.Texture;
import io.innocent.dream.inventory.ChestInventory;
import io.innocent.dream.util.DisplayManager;
import io.innocent.dream.worldBuilder.WorldBuilder;

public class ChestTile extends StorageTile {
	
	public ChestTile() {
		super(Type.WALL, new Texture("assets/tiles/tile-map.png", 64, 32, 32, 32), 
				"data/tiles/chest.iddt", new ChestInventory());
	}
	
	@Override
	public void onUse(Vector2f position) {
		inventoryShown = true;
		inventory = WorldBuilder.world.loadChestInventory(position);
	}
	
	public void hideInventory() {
		inventoryShown = false;
	}
	
	@Override
	public void renderTileAtPos(float x, float y) {
		super.renderTileAtPos(x, y);
		if (GLFW.glfwGetKey(DisplayManager.win, GLFW.GLFW_KEY_E) == GLFW.GLFW_TRUE) {
			hideInventory();
		}
	}

}
