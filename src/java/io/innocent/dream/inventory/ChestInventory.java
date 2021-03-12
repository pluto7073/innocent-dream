package io.innocent.dream.inventory;

import org.joml.Vector2i;

import io.innocent.dream.drawing.Texture;

public class ChestInventory extends AbstractInventory {

	private static final long serialVersionUID = -2650268478894112074L;

	public ChestInventory() {
		super(new Vector2i(96, 96), 3, 3,
                new Texture("assets/gui/inventory.png"), 21,
                new Vector2i(9, 12), new Vector2i(3, 3));
	}

}
