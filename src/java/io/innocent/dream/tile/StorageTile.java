package io.innocent.dream.tile;

import io.innocent.dream.drawing.Texture;
import io.innocent.dream.inventory.AbstractInventory;

public class StorageTile extends Tile {
	
	protected AbstractInventory inventory;
	protected boolean inventoryShown = false;

	public StorageTile(Type type, Texture texture, String dropTablePath, AbstractInventory inventory) {
		super(type, texture, dropTablePath);
		this.inventory = inventory;
	}

	@Override
	public void renderTileAtPos(float x, float y) {
		super.renderTileAtPos(x, y);
		if (inventoryShown) {
			inventory.renderInventory();
		}
	}

}
